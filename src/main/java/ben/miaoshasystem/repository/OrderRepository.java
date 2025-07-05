package ben.miaoshasystem.repository;

import ben.miaoshasystem.entity.MyOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<MyOrders, Integer> {
}
