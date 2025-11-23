package training.g2.controller.client;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.User.PasswordReqDTO;
import training.g2.dto.Request.User.ProfileReqDTO;
import training.g2.dto.Request.User.UserAddressReqDTO;
import training.g2.dto.Response.User.ProfileResDTO;
import training.g2.dto.Response.User.UserAddressResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.ApiResponse;
import training.g2.service.UserAddressService;
import training.g2.service.UserService;
import training.g2.util.SecurityUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final UserService userService;
    private final UserAddressService addressService;
    private final SecurityUtil securityUtil;

    private Long getUid() {
        Long uid = securityUtil.getCurrentUserId();
        if (uid == null) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
        }
        return uid;
    }

    public AccountController(UserService userService, UserAddressService addressService, SecurityUtil securityUtil) {
        this.userService = userService;
        this.addressService = addressService;
        this.securityUtil = securityUtil;
    }

    @PutMapping("/profile")
    public ApiResponse<ProfileResDTO> updateProfile(@Valid @RequestBody ProfileReqDTO dto) {
        ProfileResDTO result = userService.updateProfile(dto);
        return new ApiResponse<>("Cập nhật hồ sơ thành công", result);
    }
    @PostMapping("/password")
    public ApiResponse<Boolean> changePassword(@RequestBody PasswordReqDTO dto) {
        
        return new ApiResponse<>("Thay đổi mật khẩu thành công",userService.changePassword(dto) );
    }

    @GetMapping("/addresses")
    public ApiResponse<List<UserAddressResDTO>> listAddresses() {
        return new ApiResponse<>("OK", addressService.getMyAddresses(getUid()));
    }

    @PostMapping("/addresses")
    public ApiResponse<UserAddressResDTO> createAddress(@RequestBody UserAddressReqDTO dto) {
        return new ApiResponse<>("Thêm địa chỉ thành công",
                addressService.createAddress(getUid(), dto));
    }

    @PutMapping("/addresses/{id}")
    public ApiResponse<UserAddressResDTO> updateAddress(@PathVariable Long id,
                                                        @RequestBody UserAddressReqDTO dto) {
        return new ApiResponse<>("Cập nhật địa chỉ thành công",
                addressService.updateAddress(getUid(), id, dto));
    }

    @DeleteMapping("/addresses/{id}")
    public ApiResponse<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(getUid(), id);
        return new ApiResponse<>("Xóa địa chỉ thành công", null);
    }

    @PutMapping("/addresses/{id}/default")
    public ApiResponse<UserAddressResDTO> setDefault(@PathVariable Long id) {
        return new ApiResponse<>("Đặt địa chỉ mặc định thành công",
                addressService.setDefault(getUid(), id));
    }
}
