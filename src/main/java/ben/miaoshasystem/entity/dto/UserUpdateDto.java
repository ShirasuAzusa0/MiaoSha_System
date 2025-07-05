package ben.miaoshasystem.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    private String userName;
    private String email;
    private String self_description;
    private String avatar;
    private String password;
}
