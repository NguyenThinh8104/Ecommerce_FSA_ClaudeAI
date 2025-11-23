package training.g2.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import training.g2.dto.Request.Slider.CreateSliderRequest;
import training.g2.dto.Request.Slider.UpdateSliderRequest;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.Slider;
import training.g2.repository.SliderRepository;
import training.g2.service.SliderService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SliderServiceImpl implements SliderService {

    private final SliderRepository sliderRepository;

    @Override
    public Slider create(CreateSliderRequest req) {
        // imageUrl/redirectUrl đã là link Cloudinary trả về, chỉ lưu DB
        Slider s = Slider.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .imageUrl(req.getImageUrl())
                .redirectUrl(req.getRedirectUrl())
                .position(req.getPosition() == null ? 0 : req.getPosition())
                .active(req.getActive() == null ? true : req.getActive())
                .build();
        return sliderRepository.save(s);
    }

    @Override
    public Slider update(Long id, UpdateSliderRequest req) {
        Slider s = getById(id);

        if (req.getTitle() != null) s.setTitle(req.getTitle());
        if (req.getDescription() != null) s.setDescription(req.getDescription());
        // Lưu ý: đã có controller upload Cloudinary riêng -> ở đây chỉ set link
        if (req.getImageUrl() != null) s.setImageUrl(req.getImageUrl());
        if (req.getRedirectUrl() != null) s.setRedirectUrl(req.getRedirectUrl());
        if (req.getPosition() != null) s.setPosition(req.getPosition());
        if (req.getActive() != null) s.setActive(req.getActive());

        return sliderRepository.save(s);
    }

    @Override
    public void delete(Long id) {
        if (!sliderRepository.existsById(id)) {
            throw new IllegalArgumentException("Slider not found");
        }
        sliderRepository.deleteById(id);
    }

    @Override
    public Slider getById(Long id) {
        return sliderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Slider not found"));
    }

    @Override
    public PaginationDTO<List<Slider>> list(
            String keyword,
            Boolean active,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            String sort
    ) {
        // Parse sort: "field,dir" -> default createdAt,desc
        Sort sortSpec = parseSort(sort);

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                sortSpec
        );

        Specification<Slider> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String kw = "%" + keyword.trim().toLowerCase() + "%";
                Predicate byTitle = cb.like(cb.lower(root.get("title")), kw);
                Predicate byDescription = cb.like(cb.lower(root.get("description")), kw);
                predicates.add(cb.or(byTitle, byDescription));
            }

            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            // Lọc theo khoảng ngày dùng createdAt (không có startAt/endAt trong model)
            if (startDate != null) {
                Instant from = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            }
            if (endDate != null) {
                // endDate 23:59:59.999
                Instant to = endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusMillis(1);
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Slider> result = sliderRepository.findAll(spec, pageable);

        return PaginationDTO.<List<Slider>>builder()
                .page(result.getNumber())
                .size(result.getSize())
                .total(result.getTotalElements())
                .items(result.getContent())
                .build();
    }

    @Override
    public Slider updateActive(Long id, boolean active) {
        Slider s = getById(id);
        s.setActive(active);
        return sliderRepository.save(s);
    }

    @Override
    public Slider updateSortOrder(Long id, Integer sortOrder) {
        Slider s = getById(id);
        if (sortOrder == null) {
            throw new IllegalArgumentException("sortOrder cannot be null");
        }
        s.setPosition(sortOrder);
        return sliderRepository.save(s);
    }

    @Override
    public List<Slider> getAllSortByPosition() {
      return sliderRepository.findAllByActiveTrueOrderByPositionAsc();
    }

    private Sort parseSort(String sort) {
        String field = "createdAt";
        Sort.Direction dir = Sort.Direction.DESC;

        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",", 2);
            field = parts[0].trim();
            if (parts.length > 1) {
                String d = parts[1].trim().toLowerCase();
                if ("asc".equals(d)) dir = Sort.Direction.ASC;
                if ("desc".equals(d)) dir = Sort.Direction.DESC;
            }
        }
        // Whitelist tránh lỗi typo field
        if (!List.of("createdAt","updatedAt","title","position","active","id").contains(field)) {
            field = "createdAt";
        }
        return Sort.by(new Sort.Order(dir, field));
    }
}

