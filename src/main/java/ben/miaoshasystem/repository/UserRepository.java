package ben.miaoshasystem.repository;

import ben.miaoshasystem.entity.Users;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findById(int userId);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Users findByEmail(@Param("email") String email);

    @Query("SELECT COALESCE(MAX(u.userId), 0) FROM Users u")
    int findMaxUserId();
}
