package ben.miaoshasystem.repository;

import ben.miaoshasystem.entity.OrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderGoodsRepository extends JpaRepository<OrderGoods, Integer> {
}
