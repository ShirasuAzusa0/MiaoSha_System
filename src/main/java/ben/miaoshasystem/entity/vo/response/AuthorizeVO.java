// 响应类的vo

package ben.miaoshasystem.entity.vo.response;

// 编译期注解处理器，自动生成样板代码
import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    String username;
    String role;
    String token;
    Date expire;
}
