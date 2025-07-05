package ben.miaoshasystem.service;

import ben.miaoshasystem.entity.Goods;
import ben.miaoshasystem.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodService {

    @Autowired
    private GoodRepository goodRepository;

    // 根据商品ID查找商品信息实体
    public Goods getGoodById(int goodId) {
        return goodRepository.findById(goodId);
    }

    // 获取商品列表
    public List<Goods> getLimitedGoods(String tag, int choice, int limit) {
        return switch (choice) {
            case 1 -> goodRepository.findLimitListByGoodId(tag, limit);
            case 2 -> goodRepository.findLimitListByPrice(tag, limit);
            case 3 -> goodRepository.findLimitListByQuantity(tag, limit);
            default -> null;
        };
    }
}
