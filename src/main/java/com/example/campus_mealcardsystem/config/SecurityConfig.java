package com.example.campus_mealcardsystem.config;

import com.example.campus_mealcardsystem.security.SysUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 安全配置类（适配 Spring Boot 2.7 + Security 5.7）
 * 角色权限矩阵：
 * - ADMIN: 开卡注册/注销/备份/设置 + 所有普通功能
 * - STUDENT/TEACHER: 我的饭卡 + 充值/消费/挂失/解冻/交易记录
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SysUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        // ===== 放行区域 =====
                        .antMatchers("/login", "/doLogin").permitAll()
                        .antMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .antMatchers("/403").permitAll()

                        // ===== 管理员专属 =====
                        .antMatchers("/register", "/settings").hasRole("ADMIN")
                        .antMatchers("/api/card-holder/register", "/api/card-holder/cancel").hasRole("ADMIN")
                        .antMatchers("/api/backup").hasRole("ADMIN")

                        // ===== 普通用户专属 =====
                        .antMatchers("/my-card").hasAnyRole("STUDENT", "TEACHER")
                        .antMatchers("/api/my-card/**").hasAnyRole("STUDENT", "TEACHER")

                        // ===== 所有登录用户共享 =====
                        .antMatchers("/", "/index").authenticated()
                        .antMatchers("/recharge-consume", "/loss-management", "/transactions").authenticated()
                        .antMatchers("/api/card-holder/recharge", "/api/card-holder/consume").authenticated()
                        .antMatchers("/api/card-holder/report-loss/**", "/api/card-holder/unfreeze/**").authenticated()
                        .antMatchers("/api/transaction/**").authenticated()

                        // ===== 其余所有请求 =====
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/doLogin")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                            response.sendRedirect("/403")
                        )
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
