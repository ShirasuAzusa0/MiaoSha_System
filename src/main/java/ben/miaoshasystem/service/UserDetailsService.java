package ben.miaoshasystem.service;

import ben.miaoshasystem.entity.Users;
import ben.miaoshasystem.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 一定要继承父类UserDetailsService，然后重写其中的loadUserByUsername
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Resource
    UserRepository userRepository;

    // @Resource
    // PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查用户，示例是通过 userName
        Users users = userRepository.findByEmail(username);
        if (users == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 返回 Spring Security 的 User 对象
        return User
                .withUsername(users.getEmail())
                .password(users.getPassword())  // 从数据库读到已加密的密码
                .authorities(String.valueOf(users.getType())) // 这里可以从用户表里读角色
                .build();
    }
}
