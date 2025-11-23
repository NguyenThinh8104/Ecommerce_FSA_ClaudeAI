package training.g2.service;

import training.g2.dto.Request.Voucher.VoucherReqDTO;
import training.g2.dto.Response.User.UserEmailResDTO;
import training.g2.dto.Response.Voucher.VoucherResDTO;

import java.util.List;

public interface VoucherService {

    VoucherResDTO createVoucher(VoucherReqDTO dto);

    VoucherResDTO updateVoucher(Long id, VoucherReqDTO dto);

    void deleteVoucher(Long id);

    VoucherResDTO getVoucher(Long id);

    List<VoucherResDTO> getAllVouchers();

    void refreshExpiredStatus(); // cập nhật voucher hết hạn theo end_date

    VoucherResDTO assignToUsers(Long voucherId, List<String> emails);
    List<UserEmailResDTO> getAssignedUsers(Long voucherId);
}
