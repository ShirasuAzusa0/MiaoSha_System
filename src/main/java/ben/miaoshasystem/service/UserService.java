package ben.miaoshasystem.service;

import ben.miaoshasystem.entity.Users;
import ben.miaoshasystem.util.RSAKeyUtil;
import ben.miaoshasystem.entity.dto.UserUpdateDto;
import ben.miaoshasystem.mapper.UsersMapper;
import ben.miaoshasystem.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    RSAKeyUtil rsaKeyUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersMapper usersMapper;

    // 根据用户ID查找用户信息实体
    public Users getUserById(int userId) {
        return userRepository.findById(userId);
    }

    // 用于数据库数据的修改事务的提交
    @Transactional
    // 根据用户ID查找到当前用户并修改用户信息
    public void updateUserProfile(int userId, UserUpdateDto dto) {
        Users user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 用EntityManager找到托管实体
        Users managedUser = entityManager.find(Users.class, userId);
        if (managedUser == null) {
            throw new RuntimeException("用户不存在（托管实体）");
        }

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

        // usersMapper.updateUserFromDto(dto, managedUser);
        // 修改用户信息
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            managedUser.setPassword(dto.getPassword());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            managedUser.setEmail(dto.getEmail());
        }
        if (dto.getUserName() != null && !dto.getUserName().isEmpty()) {
            managedUser.setUserName(dto.getEmail());
        }
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            managedUser.setAvatar(dto.getAvatar());
        }
        if (dto.getSelf_description() != null && !dto.getSelf_description().isEmpty()) {
            managedUser.setSelf_description(dto.getSelf_description());
        }
        // 刷新，将数据更新进去
        entityManager.flush();
    }


}
