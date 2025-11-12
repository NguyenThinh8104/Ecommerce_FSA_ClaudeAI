package training.g2.dto.Response.Attribute;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeResDTO {
    private long id;
    private String code;
    private String name;
    private List<AttributeValueDTO> values;

    @Getter
    @Setter
    public static class AttributeValueDTO {
        private long id;
        private String value;
    }
}
