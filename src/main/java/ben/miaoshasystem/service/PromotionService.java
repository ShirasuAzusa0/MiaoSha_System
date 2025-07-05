package ben.miaoshasystem.service;

import ben.miaoshasystem.entity.Promotions;
import ben.miaoshasystem.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    // 根据活动ID查找活动信息实体
    public Promotions getPromotionById(int promotionId) {
        return promotionRepository.findById(promotionId);
    }

    // 获取秒杀活动列表
    public List<Promotions> getLimitedPromotions(int choice, int limit) {
        // PageRequest page = PageRequest.of(0, limit);
        return switch (choice) {
            case 1 -> promotionRepository.findLimitListByStartTime(limit);
            case 2 -> promotionRepository.findLimitListById(limit);
            default -> null;
        };
    }
}
