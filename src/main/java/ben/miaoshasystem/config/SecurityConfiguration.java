// Security配置文件：
// 提供安全框架——集成Spring Security，帮助开发者快速添加身份验证和授权功能
// 保护应用——防止未授权访问敏感接口（如管理后台、API等）
// 默认安全防护——开箱急用的安全防护机制（默认要求所有请求需认证、默认登录表单、CSRF防护等）

package ben.miaoshasystem.config;

import ben.miaoshasystem.entity.RestBean;
import ben.miaoshasystem.entity.filter.JwtAuthorizeFilter;
import ben.miaoshasystem.entity.filter.RSAPasswordDecryptFilter;
import ben.miaoshasystem.entity.vo.response.AuthorizeVO;
import ben.miaoshasystem.util.JwtUtils;
import ben.miaoshasystem.util.RSAKeyUtil;
import com.auth0.jwt.JWT;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    RSAKeyUtil rsaKeyUtil;

    @Resource
    RSAPasswordDecryptFilter rsaPasswordDecryptFilter;

    // 注册一个Bean
    @Bean
    // filterCain是安全过滤器链，通过build()建立一个SecurityFilterChain，3.6.1版本后要用lambda方式来写
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // .authorizeRequests()在3.6.1版本更新后已变成过时用法，不再使用
        return http
                // 设置授权规则，开启基于请求路径的授权配置，conf即为HTTP的配置
                .authorizeHttpRequests(conf -> conf
                        // 允许所有/api/auth/**的请求，其他所有请求都需要登录验证后才能访问
                        .requestMatchers("/api/auth/**").permitAll()
                        // 允许所有/goods/**的请求，其他所有请求都需要登录验证后才能访问
                        .requestMatchers("/goods/**").permitAll()
                        // 允许所有/promotion/**的请求，其他所有请求都需要登录验证后才能访问
                        .requestMatchers("promotion/**").permitAll()
                        // 公钥获取的接口无需拦截
                        .requestMatchers("/key/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 表单登录
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        // 登录失败返回数据
                        .failureHandler(this::onAuthenticationFailure)
                        // 登录成功返回数据
                        .successHandler(this::onAuthenticationSuccess)
                )
                // 登出
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                // 异常处理
                .exceptionHandling(conf -> conf
                        // 用户未登录访问需认证的接口时触发，401
                        .authenticationEntryPoint(this::onUnauthorized)
                        // 已登录用户访问没有权限的接口时触发，403
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(AbstractHttpConfigurer::disable)
                // 无状态前后端分离，session无需维护用户信息，用户信息都在jwt中
                // 因此在此处要将SessionCreationPolicy设置未STATELESS（无状态）
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 自定义JWT授权过滤器，从响应头中解析出JWT，验证Token合法性
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                // 自定义RSA解密过滤器
                .addFilterBefore(rsaPasswordDecryptFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // 登录成功返回信息
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        User user = (User)authentication.getPrincipal();
        // 创建一个JWT token
        String token = utils.createJWT(user, 1, "Azusa");
        // 生成合法Bearer前缀的token
        String bearerToken = "Bearer " + token;
        AuthorizeVO vo = new AuthorizeVO();
        vo.setExpire(JWT.decode(token).getExpiresAt());
        vo.setRole("");
        vo.setToken(bearerToken);
        vo.setUsername("Azusa");
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    // 登录失败返回信息
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 指明返回的是json数据，并且编码使用utf-8
        response.setContentType("application/json;charset=utf-8");
        // exception.getMessage()可以直接给出错误的原因
        response.getWriter().write(RestBean.failure(exception.getMessage()).asJsonString());
    }

    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

    }


    // 登录后不同用户权限处理
    private void onAccessDeny(HttpServletRequest request,
                              HttpServletResponse response,
                              AccessDeniedException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString());
    }

    // 未登录处理
    public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception) throws IOException {
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }
}
