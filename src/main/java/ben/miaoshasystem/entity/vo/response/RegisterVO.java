package ben.miaoshasystem.entity.vo.response;

import lombok.Data;

@Data
public class RegisterVO {
    private int userId;
    private String username;
    private String token;
}
