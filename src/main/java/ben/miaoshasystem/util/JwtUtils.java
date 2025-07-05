package ben.miaoshasystem.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    // 加密密钥
    @Value("${spring.security.jwt.key}")
    String key;

    // JWT有效期
    @Value("${spring.security.jwt.expire}")
    int expire;

    // 创建JWT令牌，需要用到用户的信息（从details中读取）、id、用户名
    public String createJWT(UserDetails userDetails, int id, String username) {
        // 使用HMAC256加密算法
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                // 根据userDetails获取对应的信息
                .withClaim("id", id)
                .withClaim("username", username)
                // 得到字符串类型的JWT加密串
                .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire)
                .withIssuedAt(new Date())
                // 签名，最终得到JWT
                .sign(algorithm);
    }

    // JWT令牌验证与解析方法
    public DecodedJWT resolveJWT(String headerToken) {
        // 调用convertToken方法，去掉“Bearer”前缀，并校验token是否合法
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        // 构造验证器，用来校验签名及标准字段
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            // 验证token的签名、iss、aud等，并抛出异常或返回DecodedJWT
            DecodedJWT verify = verifier.verify(token);
            Date expiresAt = verify.getExpiresAt();
            return new Date().after(expiresAt) ? null : verify;
        } catch (JWTVerificationException e) {
            // 有异常则统一返回null
            return null;
        }
    }

    // JWT令牌过期时间计算方法
    public Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    // 解析用户信息的方法
    // 将已验证的DecodedJWT中自定义的用户信息提取出来，构造出UserDetails对象
    // 用户信息是提前设定好的，要是有变化还得回来这里修改添加新的信息
    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("username").asString())
                .password("*****")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    // 解析用户Id
    // 从DecodedJWT中提取用户的数据库主键或唯一标识id，供业务层使用
    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    // token判断方法（token合法性判断）
    private String convertToken(String headerToken) {
        // 非空，或token以Bearer开头才是合法的token
        if (headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        // 从第七个位置切割，去掉“Bearer ”前缀，以得到纯粹的JWT
        return headerToken.substring(7);
    }
}
