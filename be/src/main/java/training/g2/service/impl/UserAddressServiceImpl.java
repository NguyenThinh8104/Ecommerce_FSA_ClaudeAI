package training.g2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.g2.dto.Request.User.UserAddressReqDTO;
import training.g2.dto.Response.User.UserAddressResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.*;
import training.g2.repository.*;
import training.g2.service.UserAddressService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository addressRepo;
    private final UserRepository userRepo;
    private final ProvinceRepository provinceRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;

    private UserAddressResDTO toDTO(UserAddress a) {
        UserAddressResDTO dto = new UserAddressResDTO();
        dto.setId(a.getId());
        dto.setFullName(a.getFullName());
        dto.setPhone(a.getPhone());
        dto.setProvince(a.getProvince().getName());
        dto.setDistrict(a.getDistrict().getName());
        dto.setWard(a.getWard().getName());
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
                .toList();
    }

    @Override
    @Transactional
    public UserAddressResDTO createAddress(Long userId, UserAddressReqDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User không tồn tại"));
        Province province = provinceRepo.findById(dto.getProvinceId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Tỉnh/Thành phố không tồn tại"));

        District district = districtRepo.findById(dto.getDistrictId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Quận/Huyện không tồn tại"));

        if (!district.getProvince().getId().equals(province.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Quận/Huyện không thuộc Tỉnh/Thành phố đã chọn");
        }

        Ward ward = wardRepo.findById(dto.getWardId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phường/Xã không tồn tại"));

        if (!ward.getDistrict().getId().equals(district.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Phường/Xã không thuộc Quận/Huyện đã chọn");
        }

        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            addressRepo.clearDefaultByUserId(userId);
        }

        UserAddress a = new UserAddress();
        a.setUser(user);
        a.setFullName(dto.getFullName());
        a.setPhone(dto.getPhone());
        a.setProvince(province);
        a.setDistrict(district);
        a.setWard(ward);
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
        Province province = provinceRepo.findById(dto.getProvinceId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Tỉnh/Thành phố không tồn tại"));

        District district = districtRepo.findById(dto.getDistrictId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Quận/Huyện không tồn tại"));

        if (!district.getProvince().getId().equals(province.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Quận/Huyện không thuộc Tỉnh/Thành phố đã chọn");
        }

        Ward ward = wardRepo.findById(dto.getWardId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phường/Xã không tồn tại"));

        if (!ward.getDistrict().getId().equals(district.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Phường/Xã không thuộc Quận/Huyện đã chọn");
        }

        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            addressRepo.clearDefaultByUserId(userId);
            a.setDefault(true);
        }

        a.setFullName(dto.getFullName());
        a.setPhone(dto.getPhone());
        a.setProvince(province);
        a.setDistrict(district);
        a.setWard(ward);
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

        boolean wasDefault = a.isDefault();
        addressRepo.delete(a);

        if (wasDefault) {
            addressRepo.findFirstByUser_IdOrderByCreatedAtAsc(userId)
                    .ifPresent(addr -> {
                        addr.setDefault(true);
                        addr.setUpdatedAt(Instant.now());
                        addressRepo.save(addr);
                    });
        }
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
