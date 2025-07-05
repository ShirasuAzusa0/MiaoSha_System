package ben.miaoshasystem.controller;

import ben.miaoshasystem.entity.RestBean;
import ben.miaoshasystem.entity.UserType;
import ben.miaoshasystem.entity.Users;
import ben.miaoshasystem.entity.dto.SignupDto;
import ben.miaoshasystem.entity.vo.response.RegisterVO;
import ben.miaoshasystem.repository.UserRepository;
import ben.miaoshasystem.util.JwtUtils;
import ben.miaoshasystem.util.RSAKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RSAKeyUtil rsaKeyUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@ModelAttribute SignupDto dto) {

        if (userRepository.findByEmail(dto.getAccount()) != null) {
            return ResponseEntity.badRequest().body(RestBean.failure("邮箱已被注册"));
        }

        // 找当前最大 userId
        int maxId = userRepository.findMaxUserId();
        int newId = maxId + 1;

        // 对密码进行解密
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String rawPassword = dto.getPassword();
            // 先把空格替换回加号
            String fixedPassword = rawPassword.replace(' ', '+');
            // 再去除密码中所有空白字符（空格、换行等）
            String encryptedPasswordClean = fixedPassword.replaceAll("\\s+", "");

            System.out.println(encryptedPasswordClean);

            // 再进行解密
            String decryptedPassword = rsaKeyUtil.decrypt(encryptedPasswordClean);
            dto.setPassword(decryptedPassword);
        }

        Users user = new Users();
        // 手动设置
        user.setUserId(newId);
        user.setUserName(dto.getUsername());
        user.setEmail(dto.getAccount());
        user.setPassword(dto.getPassword());
        user.setAvatar("https://avatars.githubusercontent.com/u/19370775");
        user.setSelf_description("这个人很懒，还没有写自我介绍");
        user.setRegister_time(LocalDateTime.now());
        user.setLast_connect(LocalDateTime.now());
        user.setType(UserType.user);

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        UserDetails userDetails = User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authentication.getAuthorities())
                .build();

        String token = jwtUtils.createJWT(userDetails, 1, dto.getUsername());
        String bearerToken = "Bearer " + token;
        RegisterVO vo = new RegisterVO();
        vo.setUsername(dto.getUsername());
        vo.setUserId(newId);
        vo.setToken(bearerToken);
        vo.setUsername(dto.getUsername());
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "注册成功",
                        "data", vo
                )
        );
    }
}
