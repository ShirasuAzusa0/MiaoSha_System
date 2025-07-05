package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "goods")
public class Goods {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goodId")
    private int goodId;

    @Getter
    @Setter
    @Column(name = "goodName", nullable = false)
    private String goodName;

    @Getter
    @Setter
    @Column(nullable = false)
    private int quantity;

    @Getter
    @Setter
    @Column(nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(nullable = false)
    private String image;

    @Getter
    @Setter
    @Column(nullable = false)
    private int price;

    @Getter
    @Setter
    @Lob
    @Column(nullable = false)
    private String content;

    // relations
    // goods 和 tags 的一对多关系
    @Getter
    @Setter
    @OneToMany(mappedBy = "goodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tags> tags;

    // goods 和 promotions 的多对多关系
    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name = "goods_promotions",
            joinColumns = @JoinColumn(name = "goodId"),
            inverseJoinColumns = @JoinColumn(name = "promotionId")
    )
    private List<Promotions> promotions;

    // goods 和 orders 的一对多关系
    @Getter
    @Setter
    @OneToMany(mappedBy = "goodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyOrders> orders;

    public Goods() {}
}
