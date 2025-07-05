package ben.miaoshasystem.controller;

import ben.miaoshasystem.entity.Goods;
import ben.miaoshasystem.entity.dto.OrderRequestDto;
import ben.miaoshasystem.entity.vo.response.OrderVO;
import ben.miaoshasystem.repository.GoodRepository;
import ben.miaoshasystem.repository.UserRepository;
import ben.miaoshasystem.service.GoodService;
import ben.miaoshasystem.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/good")
public class OrderController {

    @Resource
    private GoodRepository goodRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    @PostMapping("/order")
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequestDto dto, HttpServletRequest request) {
        // 从request中获取当前用户的id
        String authorization = request.getHeader("Authorization");
        DecodedJWT jwt = jwtUtils.resolveJWT(authorization);
        Integer userId = jwtUtils.toId(jwt);
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "msg", "未找到用户"));
        }

        int goodId = dto.getGoodsId();

        // 从 Redis 获取库存
        String stockKey = "goods:stock:" + goodId;
        Integer stock = (Integer) redisTemplate.opsForValue().get(stockKey);

        if (stock == null) {
            // 若 Redis 没有缓存，则从数据库中读取一次，并写入 Redis
            Goods good = goodRepository.findById(goodId);
            if (good == null) {
                return ResponseEntity.badRequest().body(Map.of("status", "fail", "msg", "未找到商品"));
            }
            stock = good.getQuantity();
            redisTemplate.opsForValue().set(stockKey, stock);
        }

        // 校验库存是否足够
        if(dto.getQuantity() <= 0 || dto.getQuantity() > stock) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "msg", "库存不足或购买数量非法"));
        }

        // 扣减缓存中的库存
        redisTemplate.opsForValue().decrement(stockKey, dto.getQuantity());

        // 计算订单金额（需查询价格）
        Goods good = goodRepository.findById(goodId);
        if (good == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "msg", "未找到商品"));
        }
        int cost = good.getPrice() * dto.getQuantity();

        // 生成唯一订单号
        String orderIdKey = "orders:id:generator";
        Long orderId = redisTemplate.opsForValue().increment(orderIdKey);

        // 拼装订单信息
        Map<String, Object> OrderInfo = new HashMap<>();
        // OrderInfo.put("orderId", orderId);
        OrderInfo.put("goodId", goodId);
        OrderInfo.put("customerId", userId);
        OrderInfo.put("quantity", dto.getQuantity());
        OrderInfo.put("cost", cost);
        OrderInfo.put("address", dto.getAddress());
        OrderInfo.put("orderTime", LocalDateTime.now().toString());

        // 订单写入队列
        redisTemplate.opsForList().rightPush("orders:queue", OrderInfo);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "msg", "下单成功，已进入队列",
                "data", OrderInfo
            )
        );
    }
}
