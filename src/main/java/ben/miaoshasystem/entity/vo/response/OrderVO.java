package ben.miaoshasystem.entity.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVO {
    private int orderId;
    private int goodsId;
    private int customerId;
    private int quantity;
    private double cost;
    private String address;
    private LocalDateTime orderTime;
}
