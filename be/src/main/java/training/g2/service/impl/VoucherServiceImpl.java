package training.g2.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.g2.constant.Constants.Message;
import training.g2.dto.Request.Voucher.VoucherReqDTO;
import training.g2.dto.Response.User.UserEmailResDTO;
import training.g2.dto.Response.Voucher.VoucherResDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.User;
import training.g2.model.Voucher;
import training.g2.model.VoucherUserUsage;
import training.g2.model.enums.VoucherApplyScopeEnum;
import training.g2.model.enums.VoucherDiscountTypeEnum;
import training.g2.model.enums.VoucherStatusEnum;
import training.g2.repository.UserRepository;
import training.g2.repository.VoucherRepository;
import training.g2.repository.VoucherUserUsageRepository;
import training.g2.service.VoucherService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherUserUsageRepository voucherUserUsageRepository;
    private final UserRepository userRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository,
                              VoucherUserUsageRepository voucherUserUsageRepository,
                              UserRepository userRepository) {
        this.voucherRepository = voucherRepository;
        this.voucherUserUsageRepository = voucherUserUsageRepository;
        this.userRepository = userRepository;
    }

    private void validateVoucher(VoucherReqDTO dto, boolean isCreate) {
        if (isCreate && voucherRepository.existsByCode(dto.getCode())) {
            throw new BusinessException(HttpStatus.CONFLICT, Message.VOUCHER_CODE_EXISTS);
        }

        if (dto.getStartDate() == null || dto.getEndDate() == null ||
                !dto.getStartDate().isBefore(dto.getEndDate())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.INVALID_DATE_RANGE);
        }

        if (dto.getDiscountValue() == null ||
                dto.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.INVALID_DISCOUNT_VALUE);
        }

        if (dto.getDiscountType() == VoucherDiscountTypeEnum.PERCENT &&
                dto.getDiscountValue().compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Phần trăm giảm không được vượt quá 100%");
        }

        if (dto.getUsageLimit() != null && dto.getUsageLimit() < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.INVALID_USAGE_LIMIT);
        }

        if (dto.getUserLimit() != null && dto.getUserLimit() < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.INVALID_USER_LIMIT);
        }
    }

    private VoucherResDTO toDTO(Voucher v) {
        VoucherResDTO dto = new VoucherResDTO();
        dto.setId(v.getId());
        dto.setCode(v.getCode());
        dto.setImageUrl(v.getImageUrl());
        dto.setDiscountType(v.getDiscountType());
        dto.setDiscountValue(v.getDiscountValue());
        dto.setMaxDiscountAmount(v.getMaxDiscountAmount());
        dto.setMinOrderValue(v.getMinOrderValue());
        dto.setUsageLimit(v.getUsageLimit());
        dto.setUsedCount(v.getUsedCount());
        dto.setUserLimit(v.getUserLimit());
        dto.setStartDate(v.getStartDate());
        dto.setEndDate(v.getEndDate());
        dto.setStatus(v.getStatus());
        dto.setApplyScope(v.getApplyScope());

        if (v.getUsageLimit() != null && v.getUsageLimit() > 0) {
            dto.setRemainingUsage(v.getUsageLimit() - v.getUsedCount());
        } else {
            dto.setRemainingUsage(null); // không giới hạn
        }
        return dto;
    }

    private void applyDtoToEntity(VoucherReqDTO dto, Voucher v, boolean isCreate) {
        if (isCreate) {
            v.setCode(dto.getCode().trim());
            v.setUsedCount(0);
        }

        v.setImageUrl(dto.getImageUrl());
        v.setDiscountType(dto.getDiscountType());
        v.setDiscountValue(dto.getDiscountValue());
        v.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        v.setMinOrderValue(dto.getMinOrderValue() != null ? dto.getMinOrderValue() : BigDecimal.ZERO);
        v.setUsageLimit(dto.getUsageLimit() != null ? dto.getUsageLimit() : 0);
        v.setUserLimit(dto.getUserLimit() != null ? dto.getUserLimit() : 0);
        v.setStartDate(dto.getStartDate());
        v.setEndDate(dto.getEndDate());
        v.setStatus(dto.getStatus() != null ? dto.getStatus() : VoucherStatusEnum.INACTIVE);
        v.setApplyScope(dto.getApplyScope() != null ? dto.getApplyScope() : VoucherApplyScopeEnum.ALL);
    }

    @Override
    @Transactional
    public VoucherResDTO createVoucher(VoucherReqDTO dto) {
        validateVoucher(dto, true);

        Voucher v = new Voucher();
        applyDtoToEntity(dto, v, true);
        voucherRepository.save(v);

        // nếu scope = ASSIGNED, gán user ngay
        if (v.getApplyScope() == VoucherApplyScopeEnum.ASSIGNED &&
                dto.getAssignedUserEmails() != null &&
                !dto.getAssignedUserEmails().isEmpty()) {
            assignToUsers(v.getId(), dto.getAssignedUserEmails());
        }

        return toDTO(v);
    }

    @Override
    @Transactional
    public VoucherResDTO updateVoucher(Long id, VoucherReqDTO dto) {
        Voucher v = voucherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, Message.VOUCHER_NOT_FOUND));

       

        VoucherApplyScopeEnum oldScope = v.getApplyScope();

        validateVoucher(dto, false);
        applyDtoToEntity(dto, v, false);
        voucherRepository.save(v);

        VoucherApplyScopeEnum newScope = v.getApplyScope();

       
        if (oldScope == VoucherApplyScopeEnum.ASSIGNED
                && newScope == VoucherApplyScopeEnum.ALL) {
            voucherUserUsageRepository.deleteAllByVoucher(v);
        }


        if (newScope == VoucherApplyScopeEnum.ASSIGNED
                && dto.getAssignedUserEmails() != null
                && !dto.getAssignedUserEmails().isEmpty()) {
            assignToUsers(v.getId(), dto.getAssignedUserEmails());
        }

        return toDTO(v);
    }


    @Override
    @Transactional
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(HttpStatus.NOT_FOUND, Message.VOUCHER_NOT_FOUND)
                );

       
        voucherUserUsageRepository.deleteAllByVoucher(voucher);

       
        voucherRepository.delete(voucher);
    }


    @Override
    @Transactional(readOnly = true)
    public VoucherResDTO getVoucher(Long id) {
        Voucher v = voucherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, Message.VOUCHER_NOT_FOUND));
        // auto update status nếu đã hết hạn
        if (v.getStatus() != VoucherStatusEnum.EXPIRED && v.isExpiredByDate()) {
            v.setStatus(VoucherStatusEnum.EXPIRED);
            voucherRepository.save(v);
        }
        return toDTO(v);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoucherResDTO> getAllVouchers() {
        List<Voucher> list = voucherRepository.findAll();
        // auto update status hết hạn
        list.forEach(v -> {
            if (v.getStatus() != VoucherStatusEnum.EXPIRED && v.isExpiredByDate()) {
                v.setStatus(VoucherStatusEnum.EXPIRED);
                voucherRepository.save(v);
            }
        });
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void refreshExpiredStatus() {
        List<Voucher> toExpire = voucherRepository
                .findByEndDateBeforeAndStatusNot(LocalDateTime.now(), VoucherStatusEnum.EXPIRED);
        toExpire.forEach(v -> v.setStatus(VoucherStatusEnum.EXPIRED));
        voucherRepository.saveAll(toExpire);
    }

    
    @Override
    @Transactional
    public VoucherResDTO assignToUsers(Long voucherId, List<String> emails) {
        Voucher v = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, Message.VOUCHER_NOT_FOUND));

        // đảm bảo scope = ASSIGNED
        if (v.getApplyScope() != VoucherApplyScopeEnum.ASSIGNED) {
            v.setApplyScope(VoucherApplyScopeEnum.ASSIGNED);
        }

        // Nếu null thì xem như list rỗng
        if (emails == null) {
            emails = List.of();
        }

        // Lấy toàn bộ mapping hiện tại
        List<VoucherUserUsage> existingUsages = voucherUserUsageRepository.findByVoucher(v);

        // set email mới (đưa về lowercase để so sánh)
        java.util.Set<String> newEmailSet = emails.stream()
                .map(e -> e == null ? "" : e.trim().toLowerCase())
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toSet());

        // XÓA những user KHÔNG còn trong danh sách mới
        for (VoucherUserUsage vu : existingUsages) {
            String userEmail = vu.getUser().getEmail();
            String userEmailLower = userEmail == null ? "" : userEmail.toLowerCase();
            if (!newEmailSet.contains(userEmailLower)) {
                voucherUserUsageRepository.delete(vu);
            }
        }

        // Nếu list mới rỗng => chỉ việc xóa hết, không cần add thêm
        if (newEmailSet.isEmpty()) {
            return toDTO(v);
        }

        // Lấy list User tương ứng với email mới
        List<User> users = emails.stream()
                .map(email -> userRepository.findByEmailAndDeletedFalse(email)
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                                "Không tìm thấy user với email: " + email)))
                .toList();

        // THÊM mapping cho những user chưa có
        for (User u : users) {
            voucherUserUsageRepository.findByVoucherAndUser(v, u)
                    .or(() -> {
                        VoucherUserUsage vu = new VoucherUserUsage();
                        vu.setVoucher(v);
                        vu.setUser(u);
                        vu.setUsedCount(0);
                        return java.util.Optional.of(voucherUserUsageRepository.save(vu));
                    });
        }

        return toDTO(v);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEmailResDTO> getAssignedUsers(Long voucherId) {
        Voucher v = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, Message.VOUCHER_NOT_FOUND));

        List<VoucherUserUsage> usages = voucherUserUsageRepository.findByVoucher(v);

        return usages.stream()
                .map(vu -> {
                    User u = vu.getUser();
                    return new UserEmailResDTO(
                            u.getId(),
                            u.getFullName(),
                            u.getEmail()
                    );
                })
                .toList();
    }

}
