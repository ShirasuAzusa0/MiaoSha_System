package ben.miaoshasystem.entity.vo.response;

import ben.miaoshasystem.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserProfileVO {
    private int userId;
    private String userName;
    private String email;
    private String avatar;
    private String self_description;
    private LocalDateTime register_time;
    private UserType type;
    private LocalDateTime last_connect;
}
