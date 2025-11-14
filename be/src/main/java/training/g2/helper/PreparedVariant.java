package training.g2.helper;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import training.g2.model.AttributeValue;

@Getter
@Setter
public class PreparedVariant {
    List<AttributeValue> values;
    double price;
    int stock;

    PreparedVariant(List<AttributeValue> values, double price, int stock) {
        this.values = values;
        this.price = price;
        this.stock = stock;
    }
}
