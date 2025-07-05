package ben.miaoshasystem.task;

import ben.miaoshasystem.entity.Goods;
import ben.miaoshasystem.entity.MyOrders;
import ben.miaoshasystem.entity.Users;
import ben.miaoshasystem.repository.GoodRepository;
import ben.miaoshasystem.repository.OrderRepository;
import ben.miaoshasystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrderBatchTask {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private ObjectMapper objectMapper;
    @Autowired
    private GoodRepository goodRepository;
    @Autowired
    private UserRepository userRepository;

    // 每隔5秒执行一次，批量从 Redis 取订单写回 MySQL
    @Scheduled(fixedRate = 5000)
    public void batchInsertOrders() {
        List<MyOrders> batch = new ArrayList<>();
        while (true) {
            Object data = redisTemplate.opsForList().leftPop("orders:queue");
            if (data == null) {
                break;
            }

            // 不建议这样做，直接反序列化成 MyOrders 实体很多复杂的关联字段容易出问题
            // MyOrders myOrders = objectMapper.convertValue(data, MyOrders.class);

            Map<String, Object> orderMap = (Map<String, Object>) data;

            int goodId = (Integer) orderMap.get("goodId");
            int customerId = (Integer) orderMap.get("customerId");
            int quantity = (Integer) orderMap.get("quantity");
            int cost = (Integer) orderMap.get("cost");
            String address = (String) orderMap.get("address");
            LocalDateTime orderTime = LocalDateTime.parse((String) orderMap.get("orderTime"));

            Goods good = goodRepository.findById(goodId);
            Users user = userRepository.findById(customerId);

            MyOrders myOrders = new MyOrders();
            myOrders.setGoodId(good);
            myOrders.setCustomerId(user);
            myOrders.setQuantity(quantity);
            myOrders.setCost(cost);
            myOrders.setAddress(address);
            myOrders.setOrderTime(orderTime);
            batch.add(myOrders);
        }

        if (!batch.isEmpty()) {
            orderRepository.saveAll(batch);
            System.out.println("批量写回 MySQL，数量：" + batch.size());
        }
    }
}
