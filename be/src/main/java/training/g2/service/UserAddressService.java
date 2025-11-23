package training.g2.service;

import training.g2.dto.Request.User.UserAddressReqDTO;
import training.g2.dto.Response.User.UserAddressResDTO;

import java.util.List;

public interface UserAddressService {
    List<UserAddressResDTO> getMyAddresses(Long userId);
    UserAddressResDTO createAddress(Long userId, UserAddressReqDTO dto);
    UserAddressResDTO updateAddress(Long userId, Long addressId, UserAddressReqDTO dto);
    void deleteAddress(Long userId, Long addressId);
    UserAddressResDTO setDefault(Long userId, Long addressId);
}

