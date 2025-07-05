package ben.miaoshasystem.controller;

import ben.miaoshasystem.entity.Users;
import ben.miaoshasystem.entity.dto.UserUpdateDto;
import ben.miaoshasystem.entity.vo.response.UserProfileVO;
import ben.miaoshasystem.service.UserService;
import ben.miaoshasystem.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 组合注解，表示该类的所有返回的对象会自动序列化未JSON或XML，写入HTTP响应体中
@RestController
// 整个UserController的公共URL前缀
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注入JwtUtils
    @Resource
    JwtUtils jwtUtils;

    // 处理HTTP的GET请求，路径中带有一个占位符{userId}
    @GetMapping("/profile/{userId}")
    // @PathVariable("userId") String userId把URL中的值注入到方法参数userId中
    public ResponseEntity<?> getUserData(@PathVariable("userId") int userId) {
        Users user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserProfileVO vo = new UserProfileVO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getAvatar(),
                user.getSelf_description(),
                user.getRegister_time(),
                user.getType(),
                user.getLast_connect()
        );
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取用户信息成功",
                        "data", vo
                )
        );
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateUser(UserUpdateDto userDto, HttpServletRequest request) {
        // 从request中获取当前用户的id
        String authorization = request.getHeader("Authorization");
        DecodedJWT jwt = jwtUtils.resolveJWT(authorization);
        Integer userId = jwtUtils.toId(jwt);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        // 调用服务执行更新逻辑
        userService.updateUserProfile(userId, userDto);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "用户信息修改成功"
                )
        );
    }
}
