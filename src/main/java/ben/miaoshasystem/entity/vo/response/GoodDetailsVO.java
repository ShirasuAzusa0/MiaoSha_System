package ben.miaoshasystem.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GoodDetailsVO {
    private int goodId;
    private String goodName;
    private int quantity;
    private String description;
    private String image;
    private double price;
    private String content;
    List<String> tags;
}
