package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private String self_description;

    @Column(nullable = false)
    private LocalDateTime register_time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('user','admin')")
    private UserType type;

    // 一个用户可有多个订单
    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders;

    public Users() {}
}
