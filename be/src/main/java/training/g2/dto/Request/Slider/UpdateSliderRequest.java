package training.g2.dto.request.Slider;

import lombok.Getter;

@Getter

public class UpdateSliderRequest {
    private String title;
    private String description;
    private String imageUrl;   // từ Cloudinary upload API riêng
    private String redirectUrl;
    private Integer position;
    private Boolean active;
}
