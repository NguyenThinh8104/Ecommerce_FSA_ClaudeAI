package training.g2.dto.Request.Ghn;

import lombok.Data;

@Data
public class GhnItem {
    private String name;
    private String code;      // mã SKU / mã nội bộ
    private Integer quantity;
    private Integer price;    // giá 1 sản phẩm
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer weight;
}