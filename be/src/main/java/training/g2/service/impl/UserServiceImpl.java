package training.g2.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import static training.g2.constant.Constants.UserExceptionInformation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static training.g2.constant.Constants.Message.*;

import training.g2.dto.Request.User.RegisterReqDTO;
import training.g2.dto.Request.User.UpdateUserReqDTO;
import training.g2.dto.Response.User.CreateUserResDTO;
import training.g2.dto.Response.User.UpdateUserResDTO;
import training.g2.dto.Response.User.UsersResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.UserMapperDTO;
import training.g2.model.Role;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;
import training.g2.model.enums.UserStatusEnum;
import training.g2.repository.RoleRepository;
import training.g2.repository.UserRepository;
import training.g2.service.EmailService;
import training.g2.service.TokenService;
import training.g2.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapperDTO userMapperDTO;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    @Value("${app.base-url}")
    private String baseUrl;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserMapperDTO userMapperDTO, RoleRepository roleRepository, TokenService tokenService,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapperDTO = userMapperDTO;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public UsersResDTO getUserById(long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(USER_NOT_FOUND_MESSAGE));

            UsersResDTO dto = userMapperDTO.toDTO(user);
            return dto;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(GET_USER_FAIL_MESSAGE, e);
        }
    }

    @Override
    public CreateUserResDTO createUser(User user) {

        try {
            if (userRepository.existsByEmailAndDeletedFalse(user.getEmail())) {
                throw new BusinessException(EMAIL_ALREADY_EXISTS_MESSAGE);
            }

            if (userRepository.existsByPhoneAndDeletedFalse(user.getPhone())) {
                throw new BusinessException(PHONE_ALREADY_EXISTS_MESSAGE);
            }

            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
            user.setStatus(UserStatusEnum.NOT_ACTIVE);
            user.setDeleted(false);
            User saveUser = userRepository.save(user);
            CreateUserResDTO dto = userMapperDTO.toCreateDTO(saveUser);
            return dto;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, CREATE_USER_FAIL_MESSAGE, e);
        }

    }

    @Override
    public UpdateUserResDTO updateUser(UpdateUserReqDTO userDTO) {
        try {
            User currentUser = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));

            userRepository.findByPhoneAndIdNot(userDTO.getPhone(), userDTO.getId())
                    .ifPresent(user -> {
                        throw new BusinessException(HttpStatus.CONFLICT, INVALID_PHONE_NUMBER);
                    });

            currentUser.setFullName(userDTO.getFullName());
            currentUser.setPhone(userDTO.getPhone());
            currentUser.setGender(userDTO.getGender());
            currentUser.setStatus(userDTO.getStatus());

            userRepository.save(currentUser);
            UpdateUserResDTO dto = userMapperDTO.toUpdateDTO(currentUser);
            return dto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, UPDATE_USER_FAIL_MESSAGE, ex);
        }
    }

    @Override
    public UsersResDTO deleteUser(long id) {
        try {
            User currentUser = userRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));
            currentUser.setDeleted(true);
            userRepository.save(currentUser);

            return userMapperDTO.toDTO(currentUser);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, ex);
        }

    }

    public Specification<User> buildSearchSpecification(
            String email, Boolean deleted, Instant fromDate, Instant toDate) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (email != null && !email.trim().isEmpty()) {
                String likeEmail = "%" + email.toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), likeEmail));
            }

            if (Boolean.TRUE.equals(deleted)) {
                predicates.add(cb.equal(root.get("deleted"), true));
            } else {
                predicates.add(cb.equal(root.get("deleted"), false));
            }

            if (fromDate != null && toDate != null) {
                predicates.add(cb.between(root.get("createdAt").as(Instant.class), fromDate, toDate));
            } else if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt").as(Instant.class), fromDate));
            } else if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt").as(Instant.class), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public PaginationDTO<List<UsersResDTO>> getAllUser(int page, int size, String email, Boolean isDeleted,
            Instant fromDate, Instant toDate, String sortField, String sortDirection) {

        try {
            Sort sort = sortDirection.equalsIgnoreCase("asc")
                    ? Sort.by(sortField).ascending()
                    : Sort.by(sortField).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Specification<User> spec = buildSearchSpecification(email, isDeleted, fromDate, toDate);

            Page<User> userPage = userRepository.findAll(spec, pageable);

            List<UsersResDTO> users = userPage.getContent()
                    .stream()
                    .map(userMapperDTO::toDTO)
                    .collect(Collectors.toList());

            PaginationDTO<List<UsersResDTO>> pagingDTO = PaginationDTO.<List<UsersResDTO>>builder()
                    .page(page + 1)
                    .size(size)
                    .total(userPage.getTotalElements())
                    .items(users)
                    .build();

            return pagingDTO;

        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, GET_ALL_USER_FAIL_MESSAGE, e);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return user;
    }

    @Override
    public CreateUserResDTO register(RegisterReqDTO registerDTO) {
        try {

            if (userRepository.existsByEmailAndDeletedFalse(registerDTO.getEmail())) {
                throw new BusinessException(EMAIL_ALREADY_EXISTS_MESSAGE);
            }

            if (userRepository.existsByEmailAndDeletedFalse(registerDTO.getPhone())) {
                throw new BusinessException(PHONE_ALREADY_EXISTS_MESSAGE);
            }
            String password = passwordEncoder.encode(registerDTO.getPassword());
            User user = new User();
            user.setFullName(registerDTO.getFullName());
            user.setEmail(registerDTO.getEmail());
            user.setPhone(registerDTO.getPhone());
            user.setPassword(password);
            user.setStatus(UserStatusEnum.NOT_ACTIVE);
            user.setDeleted(false);
            Role role = roleRepository.findByName("USER").orElseThrow(() -> new BusinessException(ROLE_NOT_FOUND));
            user.setRole(role);
            userRepository.save(user);
            CreateUserResDTO dto = userMapperDTO.toCreateDTO(user);
            String token = UUID.randomUUID().toString();
            tokenService.saveNewToken(user, token, TokenTypeEnum.ACTIVE_TOKEN);
            String activationLink = baseUrl + "/api/v1/auth/activate?token=" + token;
            emailService.sendActivationEmail(user.getEmail(), user.getFullName(), activationLink);
            return dto;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, CREATE_USER_FAIL_MESSAGE, e);
        }
    }

}
