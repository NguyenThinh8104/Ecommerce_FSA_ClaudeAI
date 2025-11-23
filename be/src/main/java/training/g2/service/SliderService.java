package training.g2.service;

import training.g2.dto.Request.Slider.CreateSliderRequest;
import training.g2.dto.Request.Slider.UpdateSliderRequest;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.Slider;

import java.time.LocalDate;
import java.util.List;

public interface SliderService {
    Slider create(CreateSliderRequest req);

    Slider update(Long id, UpdateSliderRequest req);

    void delete(Long id);

    Slider getById(Long id);

    PaginationDTO<List<Slider>> list(
            String keyword,
            Boolean active,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            String sort);

    Slider updateActive(Long id, boolean active);

    Slider updateSortOrder(Long id, Integer sortOrder);

    List<Slider> getAllSortByPosition();
}
