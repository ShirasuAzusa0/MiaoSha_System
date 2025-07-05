package ben.miaoshasystem.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDto {
    private String username;
    private String account;
    private String password;
}
