package training.g2.dto.Request.Attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeValueReq {
    private String value;
    private Attribute attribute;

    @Getter
    @Setter
    public static class Attribute {
        private long id;
    }
}
