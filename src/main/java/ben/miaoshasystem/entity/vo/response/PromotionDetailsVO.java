package ben.miaoshasystem.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PromotionDetailsVO {
    private int promotionId;
    private String promotionName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
}
