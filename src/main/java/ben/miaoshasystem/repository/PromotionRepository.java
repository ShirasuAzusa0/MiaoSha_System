package ben.miaoshasystem.repository;

import ben.miaoshasystem.entity.Promotions;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotions, Integer> {
    Promotions findById(int promotionId);

    // 按ID顺序获取活动列表
    @Query(value = "SELECT * FROM Promotions ORDER BY promotionId ASC LIMIT :limit", nativeQuery = true)
    List<Promotions> findLimitListById(@Param("limit") int limit);

    // 按开始时间获取活动列表
    @Query(value = "SELECT * FROM Promotions ORDER BY startTime ASC LIMIT :limit", nativeQuery = true)
    List<Promotions> findLimitListByStartTime(@Param("limit") int limit);
}
