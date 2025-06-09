package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goodId")
    private int goodId;

    @Column(name = "goodName", nullable = false)
    private String goodName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int price;

    @Lob
    @Column(nullable = false)
    private String content;

    // relations
    // goods 和 tags 的一对多关系
    @OneToMany(mappedBy = "goodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tags> tags;

    // goods 和 promotions 的多对多关系
    @ManyToMany
    @JoinTable(
            name = "goods_promotions",
            joinColumns = @JoinColumn(name = "goodId"),
            inverseJoinColumns = @JoinColumn(name = "promotionId")
    )
    private List<Promotions> promotions;

    // goods 和 orders 的一对多关系
    @OneToMany(mappedBy = "goodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders;

    public Goods() {}
}
