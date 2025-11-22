package training.g2.dto.request.Slider;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSliderRequest {

    @NotBlank(message = "SLIDER_TITLE_REQUIRED")
    @Size(max = 255, message = "SLIDER_TITLE_TOO_LONG")
    private String title;

    @Size(max = 2000, message = "SLIDER_DESCRIPTION_TOO_LONG")
    private String description;

    @NotBlank(message = "SLIDER_IMAGE_URL_REQUIRED")
    @URL(message = "SLIDER_IMAGE_URL_INVALID")
    @Size(max = 500, message = "SLIDER_IMAGE_URL_TOO_LONG")
    private String imageUrl;

    @URL(message = "SLIDER_REDIRECT_URL_INVALID")
    @Size(max = 500, message = "SLIDER_REDIRECT_URL_TOO_LONG")
    private String redirectUrl;

    @Min(value = 0, message = "SLIDER_POSITION_NEGATIVE")
    private Integer position;

    private Boolean active;
}
