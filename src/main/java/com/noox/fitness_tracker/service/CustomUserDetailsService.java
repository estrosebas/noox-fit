package com.noox.fitness_tracker.service;

import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService.loadUserByUsername() - Username (correo): " + username);
        
        // 'username' here is the email, as configured by default in Spring Security
        // when using email as username field.
        Cuenta cuenta = cuentaRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        System.out.println("CustomUserDetailsService.loadUserByUsername() - Cuenta encontrada: " + cuenta.getCorreo());
        System.out.println("CustomUserDetailsService.loadUserByUsername() - Hash length: " + (cuenta.getContraseña() != null ? cuenta.getContraseña().length() : "null"));
        System.out.println("CustomUserDetailsService.loadUserByUsername() - Rol: " + cuenta.getRol().getNombre());

        // Crear autoridades basadas en el rol
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + cuenta.getRol().getNombre()));

        // Spring Security's User needs: username, password, authorities
        UserDetails userDetails = new User(cuenta.getCorreo(), cuenta.getContraseña(), authorities);
        
        System.out.println("CustomUserDetailsService.loadUserByUsername() - UserDetails creado exitosamente");
        return userDetails;
    }
}
