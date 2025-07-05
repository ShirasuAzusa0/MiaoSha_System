package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class MyOrders {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private long orderId;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodId", nullable = false)
    private Goods goodId;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private Users customerId;

    @Getter
    @Setter
    @Column(nullable = false)
    private int quantity;

    @Getter
    @Setter
    @Column(nullable = false)
    private int cost;

    @Getter
    @Setter
    @Column(nullable = false)
    private String address;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime orderTime;

    public MyOrders() {}
}
