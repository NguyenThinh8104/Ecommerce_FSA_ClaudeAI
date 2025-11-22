package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.g2.dto.request.User.UserAddressReqDTO;
import training.g2.dto.response.User.UserAddressResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.User;
import training.g2.model.UserAddress;
import training.g2.repository.UserAddressRepository;
import training.g2.repository.UserRepository;
import training.g2.service.UserAddressService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository addressRepo;
    private final UserRepository userRepo;

    public UserAddressServiceImpl(UserAddressRepository addressRepo, UserRepository userRepo) {
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
    }

    private UserAddressResDTO toDTO(UserAddress a) {
        UserAddressResDTO dto = new UserAddressResDTO();
        dto.setId(a.getId());
        dto.setFullName(a.getFullName());
        dto.setPhone(a.getPhone());
        dto.setProvince(a.getProvince());
        dto.setDistrict(a.getDistrict());
        dto.setWard(a.getWard());
        dto.setAddressDetail(a.getAddressDetail());
        dto.setDefault(a.isDefault());
        dto.setCreatedAt(a.getCreatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAddressResDTO> getMyAddresses(Long userId) {
        return addressRepo.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAddressResDTO createAddress(Long userId, UserAddressReqDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User không tồn tại"));

        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            addressRepo.clearDefaultByUserId(userId);
        }

        UserAddress a = new UserAddress();
        a.setUser(user);
        a.setFullName(dto.getFullName());
        a.setPhone(dto.getPhone());
        a.setProvince(dto.getProvince());
        a.setDistrict(dto.getDistrict());
        a.setWard(dto.getWard());
        a.setAddressDetail(dto.getAddressDetail());
        a.setDefault(Boolean.TRUE.equals(dto.getIsDefault()));
        a.setCreatedAt(Instant.now());
        a.setUpdatedAt(Instant.now());

        addressRepo.save(a);
        return toDTO(a);
    }

    @Override
    @Transactional
    public UserAddressResDTO updateAddress(Long userId, Long addressId, UserAddressReqDTO dto) {
        UserAddress a = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ"));

        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            addressRepo.clearDefaultByUserId(userId);
            a.setDefault(true);
        }

        a.setFullName(dto.getFullName());
        a.setPhone(dto.getPhone());
        a.setProvince(dto.getProvince());
        a.setDistrict(dto.getDistrict());
        a.setWard(dto.getWard());
        a.setAddressDetail(dto.getAddressDetail());
        a.setUpdatedAt(Instant.now());

        addressRepo.save(a);
        return toDTO(a);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress a = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ"));
        addressRepo.delete(a);
    }

    @Override
    @Transactional
    public UserAddressResDTO setDefault(Long userId, Long addressId) {
        UserAddress a = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ"));

        addressRepo.clearDefaultByUserId(userId);
        a.setDefault(true);
        a.setUpdatedAt(Instant.now());
        addressRepo.save(a);

        return toDTO(a);
    }
}
