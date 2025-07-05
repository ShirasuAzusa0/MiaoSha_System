package ben.miaoshasystem.repository;

import ben.miaoshasystem.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodRepository extends JpaRepository<Goods, Integer> {
    Goods findById(int goodId);

    // 按ID顺序获取商品列表
    @Query(value = "SELECT goods.* FROM goods JOIN tags ON goods.goodId = tags.goodId WHERE tagName = :tag ORDER BY goods.goodId ASC LIMIT :limit", nativeQuery = true)
    List<Goods> findLimitListByGoodId(String tag, int limit);

    // 按价格顺序获取商品列表
    @Query(value = "SELECT goods.* FROM goods JOIN tags ON goods.goodId = tags.goodId WHERE tagName = :tag ORDER BY goods.price ASC LIMIT :limit", nativeQuery = true)
    List<Goods> findLimitListByPrice(String tag, int limit);

    // 按照剩余数量排序获取商品列表
    @Query(value = "SELECT goods.* FROM goods JOIN tags ON goods.goodId = tags.goodId WHERE tagName = :tag ORDER BY goods.quantity ASC LIMIT :limit", nativeQuery = true)
    List<Goods> findLimitListByQuantity(String tag, int limit);
}
