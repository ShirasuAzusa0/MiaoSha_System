package ben.miaoshasystem.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private List<OrderItem> goods;

    @Data
    public static class OrderItem {
        private int goodsId;
        private int quantity;
    }
    private String address;
}
