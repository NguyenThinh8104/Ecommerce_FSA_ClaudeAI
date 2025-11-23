package training.g2.controller.admin;

import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Voucher.AssignVoucherUsersReq;
import training.g2.dto.Request.Voucher.VoucherReqDTO;
import training.g2.dto.Response.User.UserEmailResDTO;
import training.g2.dto.Response.Voucher.VoucherResDTO;
import training.g2.model.ApiResponse;
import training.g2.service.VoucherService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/vouchers")
public class VoucherAdminController {

    private final VoucherService voucherService;

    public VoucherAdminController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // Lấy tất cả voucher
    @GetMapping
    public ApiResponse<List<VoucherResDTO>> getAll() {
        return new ApiResponse<>("OK", voucherService.getAllVouchers());
    }

    // Lấy chi tiết 1 voucher
    @GetMapping("/{id}")
    public ApiResponse<VoucherResDTO> getOne(@PathVariable Long id) {
        return new ApiResponse<>("OK", voucherService.getVoucher(id));
    }

    // Tạo mới voucher
    @PostMapping
    public ApiResponse<VoucherResDTO> create(@RequestBody VoucherReqDTO dto) {
        VoucherResDTO res = voucherService.createVoucher(dto);
        return new ApiResponse<>("Tạo voucher thành công", res);
    }

    // Cập nhật voucher (chỉ khi chưa startDate)
    @PutMapping("/{id}")
    public ApiResponse<VoucherResDTO> update(@PathVariable Long id,
                                             @RequestBody VoucherReqDTO dto) {
        VoucherResDTO res = voucherService.updateVoucher(id, dto);
        return new ApiResponse<>("Cập nhật voucher thành công", res);
    }

    // Xóa voucher
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return new ApiResponse<>("Xóa voucher thành công", null);
    }

    // Gán voucher cho danh sách user (scope ASSIGNED)
    // body: ["user1@gmail.com","user2@gmail.com"]
    @PostMapping("/{voucherId}/assign-users")
    public ApiResponse<Void> assignVoucherToUsers(
            @PathVariable Long voucherId,
            @RequestBody AssignVoucherUsersReq req
    ) {
        voucherService.assignToUsers(voucherId, req.getEmails());
        return new ApiResponse<>("Gán voucher cho người dùng thành công", null);
    }
    // Lấy danh sách user đã được gán cho voucher
    @GetMapping("/{voucherId}/assigned-users")
    public ApiResponse<List<UserEmailResDTO>> getAssignedUsers(
            @PathVariable Long voucherId
    ) {
        List<UserEmailResDTO> users = voucherService.getAssignedUsers(voucherId);
        return new ApiResponse<>("OK", users);
    }
    


}
