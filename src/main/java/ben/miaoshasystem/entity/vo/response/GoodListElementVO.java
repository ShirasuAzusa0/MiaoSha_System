package ben.miaoshasystem.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodListElementVO {
    private int goodId;
    private String goodName;
    private String description;
    private double price;
    private String image;
    private int quantity;
}
