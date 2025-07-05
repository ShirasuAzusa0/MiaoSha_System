package ben.miaoshasystem.controller;

import ben.miaoshasystem.entity.Promotions;
import ben.miaoshasystem.entity.vo.response.PromotionDetailsVO;
import ben.miaoshasystem.entity.vo.response.PromotionListElementVO;
import ben.miaoshasystem.service.PromotionService;
import ben.miaoshasystem.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionDetailsVO> getPromotionData(@PathVariable int promotionId) {
        Promotions promotion = promotionService.getPromotionById(promotionId);
        if (promotion == null) {
            return ResponseEntity.notFound().build();
        }
        PromotionDetailsVO vo = new PromotionDetailsVO(
                promotion.getPromotionId(),
                promotion.getPromotionName(),
                promotion.getStartTime(),
                promotion.getEndTime(),
                promotion.getContent()
        );
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPromotionsList(int choice, @RequestParam(defaultValue = "10") int limit) {
        List<Promotions> promotions = promotionService.getLimitedPromotions(choice, limit);

        List<PromotionListElementVO> vos = promotions.stream()
                .map(p -> new PromotionListElementVO(
                        p.getPromotionId(),
                        p.getPromotionName(),
                        p.getStartTime(),
                        p.getEndTime()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "查询成功",
                        "promotions", vos
                )
        );
    }
}
