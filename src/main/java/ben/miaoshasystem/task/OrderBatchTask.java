package ben.miaoshasystem.task;

import ben.miaoshasystem.entity.Goods;
import ben.miaoshasystem.entity.MyOrders;
import ben.miaoshasystem.entity.OrderGoods;
import ben.miaoshasystem.entity.Users;
import ben.miaoshasystem.repository.GoodRepository;
import ben.miaoshasystem.repository.OrderRepository;
import ben.miaoshasystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void batchInsertOrders() {
        List<MyOrders> batch = new ArrayList<>();
        while (true) {
            Object data = redisTemplate.opsForList().leftPop("orders:queue");
            if (data == null) {
                break;
            }

            // 不建议这样做，直接反序列化成 MyOrders 实体很多复杂的关联字段容易出问题
            // MyOrders myOrders = objectMapper.convertValue(data, MyOrders.class);

            List<OrderGoods> orderGoodsList = new ArrayList<>();

            Map<String, Object> orderMap = (Map<String, Object>) data;

            int customerId = (Integer) orderMap.get("customerId");
            String address = (String) orderMap.get("address");
            LocalDateTime orderTime = LocalDateTime.parse((String) orderMap.get("orderTime"));
            double totalCost = (Double) orderMap.get("totalCost");

            Users user = userRepository.findById(customerId);

            List<Map<String, Object>> items = (List<Map<String, Object>>) orderMap.get("data");

            MyOrders myOrders = new MyOrders();
            myOrders.setCustomerId(user);
            myOrders.setAddress(address);
            myOrders.setOrderTime(orderTime);
            myOrders.setTotalCost(totalCost);

            for (Map<String, Object> item : items) {
                int goodId = (Integer) item.get("goodId");
                int quantity = (Integer) item.get("quantity");
                double cost = (Double) item.get("cost");

                Goods good = goodRepository.findById(goodId);

                // 库存扣除
                if (good != null) {
                    int newQuantity = good.getQuantity() - quantity;
                    if (newQuantity < 0) {
                        throw new RuntimeException("商品 [" + good.getGoodName() + "] 库存不足！");
                    }
                    good.setQuantity(newQuantity);
                    goodRepository.save(good);
                }

                OrderGoods orderGoods = new OrderGoods();
                orderGoods.setGoodId(good);
                orderGoods.setQuantity(quantity);
                orderGoods.setCost(cost);
                orderGoods.setOrderId(myOrders); // 先放到内存关系里

                orderGoodsList.add(orderGoods);

                myOrders.setOrderGoodsList(orderGoodsList);
                batch.add(myOrders);
            }
        }

        if (!batch.isEmpty()) {
            orderRepository.saveAll(batch);
            System.out.println("批量写回 MySQL，数量：" + batch.size());
        }
    }
}
