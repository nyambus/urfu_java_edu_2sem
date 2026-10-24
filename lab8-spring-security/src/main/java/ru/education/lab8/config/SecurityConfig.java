package ru.education.lab8.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.education.lab8.service.UserService;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/index", "/register", "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/register/save").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/list", true).permitAll()
            .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner defaultRole(UserService users) {
        return args -> users.ensureDefaultRole();
    }
}
