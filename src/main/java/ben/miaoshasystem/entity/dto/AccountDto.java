package ben.miaoshasystem.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountDto {
    private int userId;
    private String userName;
    private String password;
    private String email;
    private LocalDateTime register_time;
    private LocalDateTime last_connect;
}
