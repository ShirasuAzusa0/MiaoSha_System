package ben.miaoshasystem.entity.filter;

import ben.miaoshasystem.util.RSAKeyUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RSAPasswordDecryptFilter extends OncePerRequestFilter {

    private final RSAKeyUtil rsaKeyUtil;

    public RSAPasswordDecryptFilter(RSAKeyUtil rsaKeyUtil) {
        this.rsaKeyUtil = rsaKeyUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 处理登录接口
        if ("/api/auth/login".equals(request.getServletPath())
                && "POST".equalsIgnoreCase(request.getMethod())) {

            // 从表单获取邮箱和加密密码
            String encryptedPassword = request.getParameter("password");

            try {
                // 用私钥解密
                String decryptedPassword = rsaKeyUtil.decrypt(encryptedPassword);

                // 包装 request，把 password 替换掉
                HttpServletRequest wrapper = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getParameter(String name) {
                        if ("password".equals(name)) {
                            return decryptedPassword;
                        }
                        return super.getParameter(name);
                    }
                };

                // 放行，交给后面的 UsernamePasswordAuthenticationFilter
                filterChain.doFilter(wrapper, response);
                return;

            } catch (Exception e) {
                throw new RuntimeException("RSA 密码解密失败", e);
            }
        }

        // 不是登录请求就直接放行
        filterChain.doFilter(request, response);
    }
}
