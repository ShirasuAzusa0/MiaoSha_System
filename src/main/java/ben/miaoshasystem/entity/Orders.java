package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private int orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodId", nullable = false)
    private Goods goodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private Users customerId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    public Orders() {}
}
