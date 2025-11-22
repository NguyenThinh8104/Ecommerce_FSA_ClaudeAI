package training.g2.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.g2.model.ApiResponse;
import training.g2.model.Slider;
import training.g2.service.impl.SliderServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sliders")
@RequiredArgsConstructor
public class SliderController {
    private final SliderServiceImpl sliderService;
    @GetMapping
    public ApiResponse<List<Slider>> getSliders() {
        return ApiResponse.<List<Slider>>builder()
                .data(sliderService.getAllSortByPosition())
                .message("Lấy danh sách slider thành công")
                .build();

    }
}
