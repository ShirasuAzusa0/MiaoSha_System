package ben.miaoshasystem.entity.dto;

import lombok.Data;

@Data
public class OrderRequestDto {
    private int goodsId;
    private int quantity;
    private String address;
}
