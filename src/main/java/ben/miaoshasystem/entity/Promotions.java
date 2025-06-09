package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
public class Promotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionId")
    private int promotionId;

    @Column(name = "promotionName", nullable = false)
    private String promotionName;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Lob
    @Column(nullable = false)
    private String content;

    // relations
    // goods 和 promotions 的多对多关系反向
    @ManyToMany(mappedBy = "promotions")
    private List<Goods> goodsList;

    public Promotions() {}
}
