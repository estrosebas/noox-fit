package com.noox.fitness_tracker.service;

import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // For authorities if not using roles yet

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 'username' here is the email, as configured by default in Spring Security
        // when using email as username field.
        Cuenta cuenta = cuentaRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // For now, using an empty list of authorities.
        // Later, this can be expanded to include roles/permissions from the database.
        // Spring Security's User needs: username, password, authorities
        return new User(cuenta.getCorreo(), cuenta.getContraseña(), new ArrayList<>());
    }
}
