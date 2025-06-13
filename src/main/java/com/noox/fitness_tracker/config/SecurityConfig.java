package com.noox.fitness_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // Disable CSRF for API endpoints
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/img/**", "/error/**", "/home", "/", "/contactos/**", "/api/promotions").permitAll() // Permit static, auth, home, root and contact form
                .requestMatchers("/user", "/user/**").authenticated() // Example: secure user dashboard
                .anyRequest().authenticated() // All other requests need authentication (can be adjusted)
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/home", true) // Redirect to /home on success
                .failureUrl("/login?error=true") // Redirect to /login?error on failure
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // Redirect to /login?logout on successful logout
                .permitAll()
            );
        return http.build();
    }
}
