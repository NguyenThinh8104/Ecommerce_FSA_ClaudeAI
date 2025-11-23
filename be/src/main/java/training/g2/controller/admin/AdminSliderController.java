package training.g2.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Slider.CreateSliderRequest;
import training.g2.dto.Request.Slider.UpdateSliderRequest;

import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.model.Slider;
import training.g2.service.SliderService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/sliders")
@RequiredArgsConstructor
public class AdminSliderController {

    private final SliderService sliderService;

    @PostMapping
    public ApiResponse<Slider> create(@RequestBody CreateSliderRequest req) {
        // req.imageUrl: đã là link từ Cloudinary
        return new ApiResponse<>("Created", sliderService.create(req));
    }

    @PatchMapping("/{id}")
    public ApiResponse<Slider> update(@PathVariable Long id, @RequestBody UpdateSliderRequest req) {
        return new ApiResponse<>("Updated", sliderService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sliderService.delete(id);
        return new ApiResponse<>("Deleted", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Slider> getById(@PathVariable Long id) {
        return new ApiResponse<>("OK", sliderService.getById(id));
    }

    // List + filter + date range + paging + sort (field,dir)
    @GetMapping
    public ApiResponse<PaginationDTO<List<Slider>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        return new ApiResponse<>("OK", sliderService.list(keyword, active, startDate, endDate, page, size, sort));
    }

    @PatchMapping("/{id}/active")
    public ApiResponse<Slider> updateActive(@PathVariable Long id, @RequestParam boolean active) {
        return new ApiResponse<>("OK", sliderService.updateActive(id, active));
    }

    @PatchMapping("/{id}/position")
    public ApiResponse<Slider> updatePosition(@PathVariable Long id, @RequestParam Integer position) {
        return new ApiResponse<>("OK", sliderService.updateSortOrder(id, position));
    }
}
