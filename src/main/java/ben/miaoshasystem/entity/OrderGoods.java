package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_goods")
@Getter
@Setter
public class OrderGoods {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // 外键：订单ID（多对一）
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private MyOrders orderId;

    // 外键：商品ID（多对一）
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodId")
    private Goods goodId;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private double cost;

    public OrderGoods() {}
}
