package ben.miaoshasystem.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tags")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId")
    private int tagId;

    @Getter
    @Column(name = "tagName")
    private String tagName;

    // relations
    // tags 和 goods 的多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodId", nullable = false)
    private Goods goodId;


    public Tags() {}
}
