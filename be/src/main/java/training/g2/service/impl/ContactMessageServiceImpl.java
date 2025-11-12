package training.g2.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.ContactMessage.CreateContactMessageReq;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.ContactMessage;
import training.g2.repository.ContactMessageRepository;
import training.g2.service.ContactMessageService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static training.g2.constant.Constants.Message.CONTACT_MESSAGE_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepo;

    @Override
    public ContactMessage create(CreateContactMessageReq req, String ua, String xfwdFor, HttpServletRequest http) {
        ContactMessage m = new ContactMessage();
        m.setFullName(req.fullName());
        m.setEmail(req.email());
        m.setPhone(req.phone());
        m.setSubject(req.subject());
        m.setMessage(req.message());
        m.setIpAddress(xfwdFor != null ? xfwdFor : http.getRemoteAddr());
        m.setUserAgent(ua);
        return contactMessageRepo.save(m);
    }

    @Override
    public PaginationDTO<List<ContactMessage>> list(
            ContactMessage.Status status,
            String search,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            String sort) {
        // ----- 1. Parse sort -----
        Sort sortSpec = parseSort(sort); // ví dụ "createdAt,desc"

        // ----- 2. Pageable -----
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sortSpec);

        // ----- 3. Chuyển LocalDate -> Instant -----
        Instant from = null, toExclusive = null;
        ZoneId zone = ZoneId.systemDefault(); // hoặc ZoneOffset.UTC nếu muốn chuẩn UTC

        if (startDate != null) {
            from = startDate.atStartOfDay(zone).toInstant();
        }
        if (endDate != null) {
            toExclusive = endDate.plusDays(1).atStartOfDay(zone).toInstant();
        }

        // ----- 4. Build Specification -----
        Specification<ContactMessage> spec = statusEq(status)
                .and(searchLike(search))
                .and(createdFrom(from))
                .and(createdTo(toExclusive));

        // ----- 5. Query -----
        Page<ContactMessage> p = contactMessageRepo.findAll(spec, pageable);

        // ----- 6. Build Pagination result -----
        return PaginationDTO.<List<ContactMessage>>builder()
                .page(p.getNumber())
                .size(p.getSize())
                .total(p.getTotalElements())
                .items(p.getContent())
                .build();
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank())
            return Sort.by(Sort.Direction.DESC, "createdAt");
        String[] parts = sort.split(",", 2);
        String field = parts[0].trim();
        Sort.Direction dir = (parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(dir, field);
    }

    public ContactMessage get(Long id) {

        return contactMessageRepo.findById(id).orElseThrow(
                () -> new BusinessException(CONTACT_MESSAGE_NOT_FOUND));
    }

    @Override
    public ContactMessage updateStatus(Long id, ContactMessage.Status status) {
        ContactMessage m = get(id);
        m.setStatus(status);
        return contactMessageRepo.save(m);
    }

    @Override
    public void delete(Long id) {
        contactMessageRepo.deleteById(id);
    }

    public long bulkDelete(List<Long> ids) {
        long before = contactMessageRepo.count();
        contactMessageRepo.deleteAllById(ids);
        return Math.max(0, before - contactMessageRepo.count());
    }

    @Override
    public long bulkUpdateStatus(List<Long> ids, ContactMessage.Status status) {
        List<ContactMessage> list = contactMessageRepo.findAllById(ids);
        list.forEach(m -> m.setStatus(status));
        contactMessageRepo.saveAll(list);
        return list.size();
    }

    // ====== Các điều kiện nhỏ (có thể tái sử dụng) ======

    /** Lọc theo trạng thái */
    private Specification<ContactMessage> statusEq(ContactMessage.Status status) {
        return (root, query, cb) -> {
            if (status == null)
                return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    /** Tìm kiếm theo fullName, email, hoặc subject */
    private Specification<ContactMessage> searchLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank())
                return cb.conjunction();
            String like = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), like),
                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(cb.lower(root.get("subject")), like));
        };
    }

    /** Lọc từ thời điểm 'from' */
    private Specification<ContactMessage> createdFrom(Instant from) {
        return (root, query, cb) -> {
            if (from == null)
                return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("createdAt").as(Instant.class), from);
        };
    }

    /** Lọc đến thời điểm 'to' */
    private Specification<ContactMessage> createdTo(Instant to) {
        return (root, query, cb) -> {
            if (to == null)
                return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("createdAt").as(Instant.class), to);
        };
    }

    /** Lọc trong khoảng (from - to) */
    // private Specification<ContactMessage> createdBetween(Instant from, Instant
    // to) {
    // return (root, query, cb) -> {
    // if (from == null || to == null)
    // return cb.conjunction();
    // return cb.between(root.get("createdAt").as(Instant.class), from, to);
    // };
    // }

}
