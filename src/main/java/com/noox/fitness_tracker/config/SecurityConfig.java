package com.noox.fitness_tracker.config;

import com.noox.fitness_tracker.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // Disable CSRF for API endpoints
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/img/**", "/error/**", "/home", "/", "/contactos/**", "/api/promotions", "/api/auth/**").permitAll() // Permit static, auth, home, root and contact form
                .requestMatchers("/user", "/user/**").authenticated() // Example: secure user dashboard
                .anyRequest().authenticated() // All other requests need authentication (can be adjusted)
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/home", true) // Redirect to /home on success
                .failureUrl("/login?error=true") // Redirect to /login?error on failure
                .usernameParameter("correo") // Use email as username parameter
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // Redirect to /login?logout on successful logout
                .permitAll()
            )
            .userDetailsService(customUserDetailsService); // Use custom UserDetailsService
        return http.build();
    }
}
