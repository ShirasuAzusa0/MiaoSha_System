package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {
    // —————— getter 和 setter ——————
    @Getter
    @Setter
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Getter
    @Setter
    @Column(name = "userName", nullable = false)
    private String userName;

    @Getter
    @Setter
    @Column(nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    @Column(nullable = false)
    private String avatar;

    @Getter
    @Setter
    @Column(nullable = false)
    private String self_description;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime register_time;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('user','admin')")
    private UserType type;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDateTime last_connect;

    // 一个用户可有多个订单
    @Getter
    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyOrders> orders;

    public Users() {}

}
