package training.g2.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import static training.g2.constant.Constants.UserExceptionInformation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static training.g2.constant.Constants.Message.*;

import training.g2.dto.Request.User.PasswordReqDTO;
import training.g2.dto.Request.User.ProfileReqDTO;
import training.g2.dto.Request.User.RegisterReqDTO;
import training.g2.dto.Request.User.UpdateUserReqDTO;
import training.g2.dto.Response.User.CreateUserResDTO;
import training.g2.dto.Response.User.ProfileResDTO;
import training.g2.dto.Response.User.UpdateUserResDTO;
import training.g2.dto.Response.User.UsersResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.UserMapperDTO;
import training.g2.model.Role;
import training.g2.model.Token;
import training.g2.model.User;
import training.g2.model.enums.TokenTypeEnum;
import training.g2.model.enums.UserStatusEnum;
import training.g2.repository.RoleRepository;
import training.g2.repository.TokenRepository;
import training.g2.repository.UserRepository;
import training.g2.service.EmailService;
import training.g2.service.TokenService;
import training.g2.service.UserService;
import training.g2.dto.Response.User.*;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapperDTO userMapperDTO;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserMapperDTO userMapperDTO, RoleRepository roleRepository, TokenService tokenService,
            EmailService emailService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapperDTO = userMapperDTO;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
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
            user.setDeleted(false);
            Role role = roleRepository.findByName("USER").get();
            user.setRole(role);
            user.setStatus(UserStatusEnum.NOT_ACTIVE);
            user.setProvider("ADMIN_CREATE");
            User saveUser = userRepository.save(user);
            CreateUserResDTO dto = userMapperDTO.toCreateDTO(saveUser);

            String token = tokenService.generatePasswordUpdateToken(saveUser);
            String link = frontendUrl + "/update-password?token=" + token;

            emailService.sendUpdatePasswordEmail(
                    saveUser.getEmail(),
                    saveUser.getFullName(),
                    link,
                    10);

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
            user.setGender(registerDTO.getGender());
            user.setProvider("LOCAL");
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

    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name).get();
        return role;
    }

    @Override
    public CreateUserResDTO saveUser(User user) {
        User saveUser = userRepository.save(user);
        CreateUserResDTO dto = userMapperDTO.toCreateDTO(saveUser);
        return dto;
    }

    @Override
    public User findByEmail(String mail) {
        Optional<User> opUser = userRepository.findByEmailAndDeletedFalse(mail);
        if (opUser.isPresent()) {
            User user = opUser.get();
            return user;
        }
        return null;
    }

    @Override
    public void resendUpdatePassword(String oldToken) {
        Token existingToken = tokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new BusinessException("Token không tồn tại hoặc đã bị xoá"));

        User user = existingToken.getUser();

        tokenRepository.delete(existingToken);

        String token = tokenService.generatePasswordUpdateToken(user);

        String updateLink = frontendUrl + "/update-password?token=" + token;

        emailService.sendUpdatePasswordEmail(
                user.getEmail(),
                user.getFullName(),
                updateLink, 10);

    }
    private Long extractUserIdFromJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            Number n = jwt.getClaim("uid"); // claim "uid" do bạn set trong SecurityUtil
            if (n != null) return n.longValue();
        }
        return null;
    }

    @Override
    @Transactional
    public ProfileResDTO updateProfile(ProfileReqDTO profileDTO) {
        try {
            Long userId = extractUserIdFromJwt();
            if (userId == null) {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ hoặc thiếu JWT");
            }

            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));


            if (userRepository.existsByPhoneAndDeletedFalse(profileDTO.getPhone()) && (currentUser.getId()!=
                    userRepository.findByPhoneAndDeletedFalse(profileDTO.getPhone()).get().getId()))  {
                throw new BusinessException(PHONE_ALREADY_EXISTS_MESSAGE);
            }

            currentUser.setFullName(profileDTO.getFullName().trim());
            currentUser.setPhone(profileDTO.getPhone().trim());
            currentUser.setGender(profileDTO.getGender());
            if (profileDTO.getAvatar() != null && !profileDTO.getAvatar().isEmpty()) {
                currentUser.setAvatar(profileDTO.getAvatar());
            }

            return userMapperDTO.toProfileDTO(currentUser);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, UPDATE_USER_FAIL_MESSAGE, ex);
        }
    }


    @Override
    @Transactional
    public boolean changePassword(PasswordReqDTO req) {

        Long userId = extractUserIdFromJwt();
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        }
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, INVALID_MATCH_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<UserEmailResDTO> searchUsersByEmail(String email) {

        String term = (email == null) ? "" : email.trim();
        if (term.isEmpty()) {
            return List.of();
        }


        List<User> users = userRepository
                .findTop10ByEmailContainingIgnoreCaseAndDeletedFalse(term);


        return users.stream()
                .map(u -> new UserEmailResDTO(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail()
                ))
                .toList();
    }


}
