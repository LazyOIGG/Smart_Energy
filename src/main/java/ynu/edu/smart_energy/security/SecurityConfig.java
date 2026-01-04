package ynu.edu.smart_energy.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ynu.edu.smart_energy.security.jwt.JwtAuthFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 关闭 CSRF
                .csrf(csrf -> csrf.disable())

                // 允许跨域（前端 9091 访问后端 9090）
                .cors(cors -> {})

                // 无状态 Session（JWT 必须）
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 权限控制
                .authorizeHttpRequests(auth -> auth
                        // 登录接口放行
                        .requestMatchers("/api/auth/**").permitAll()

                        // Knife4j / Swagger 放行
                        .requestMatchers(
                                "/doc.html",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/favicon.ico"
                        ).permitAll()

                        // 普通 USER / ADMIN 允许 GET
                        .requestMatchers(HttpMethod.GET, "/api/**")
                        .hasAnyRole("USER", "ADMIN")

                        // 其余非 GET 操作必须 ADMIN
                        .requestMatchers("/api/**")
                        .hasRole("ADMIN")

                        // 其他所有请求必须登录
                        .anyRequest().authenticated()
                );

        // JWT 过滤器放 UsernamePasswordAuthenticationFilter 之前
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
