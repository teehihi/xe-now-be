package com.rental.config;

import com.rental.entity.Customer;
import com.rental.entity.Manager;
import com.rental.repository.CustomerRepository;
import com.rental.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Try Manager first
            var manager = managerRepository.findByUsername(username);
            if (manager.isPresent()) {
                Manager m = manager.get();
                return User.withUsername(m.getUsername())
                        .password(m.getPasswordHash())
                        .roles("MANAGER")
                        .build();
            }
            // Then try Customer
            var customer = customerRepository.findByEmail(username);
            if (customer.isPresent()) {
                Customer c = customer.get();
                return User.withUsername(c.getEmail())
                        .password(c.getPasswordHash())
                        .roles("CUSTOMER")
                        .build();
            }
            throw new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/static/**", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        .requestMatchers("/api/auth/**", "/api/vehicles/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("MANAGER")
                        .requestMatchers("/api/bookings/**").hasAnyRole("CUSTOMER", "MANAGER")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"Đăng nhập thành công\",\"authenticated\":true}");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter()
                                    .write("{\"message\":\"Sai tài khoản hoặc mật khẩu\",\"authenticated\":false}");
                        })
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"Đăng xuất thành công\"}");
                        })
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter()
                                    .write("{\"message\":\"Bạn cần đăng nhập để thực hiện hành động này\"}");
                        }));
        return http.build();
    }
}
