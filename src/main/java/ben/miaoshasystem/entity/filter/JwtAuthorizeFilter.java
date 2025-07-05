package ben.miaoshasystem.entity.filter;

import ben.miaoshasystem.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// 自定义的JWT授权过滤器
// 继承一次请求filter，确保每次请求只执行一次
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    // 注入JwtUtils
    @Resource
    JwtUtils jwtUtils;

    // 核心方法，三个参数分别为：当前HTTP请求、当前HTTP响应、过滤器链（用于把请求继续传递给下一个过滤器或最终的资源）
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 验证逻辑，从请求头中读取JWT
        String authorization = request.getHeader("Authorization");
        // 解析与验证JWT的合法性和过期与否
        DecodedJWT jwt = jwtUtils.resolveJWT(authorization);
        if (jwt != null) {
            // 将解析出来的用户信息给到UserDetails类对象
            UserDetails user = jwtUtils.toUser(jwt);
            // 创建一个认证凭据实现类，其包括三个构造参数：
            // Principal（主体）：第一个参数user
            // Credentials（凭证）：第二个参数null，通常是密码，由于基于JWT验证，不需要再校验密码，故直接传null
            // Authorities（权限）：第三个参数user.getAuthorities()，即用户所拥有的角色权限，用于后续的授权判断
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // 设置请求细节，直接用WebAuthenticationDetailsSource实现，将与当前HTTP请求相关的额外信息存入Authentication，日后可以通过.getDetails()拿到这里的信息
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将用户权限身份写入 Spring Security 的上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 方便快速获取用户ID
            request.setAttribute("id", jwtUtils.toId(jwt));
        }
        // 把请求继续传递下去(很重要的一步)
        filterChain.doFilter(request, response);
    }
}
