package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
public class Promotions {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionId")
    private int promotionId;

    @Getter
    @Column(name = "promotionName", nullable = false)
    private String promotionName;

    @Getter
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Getter
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Getter
    @Lob
    @Column(nullable = false)
    private String content;

    // relations
    // goods 和 promotions 的多对多关系反向
    @Getter
    @ManyToMany(mappedBy = "promotions")
    private List<Goods> goodsList;

    public Promotions() {}
}
