package com.noox.fitness_tracker.service;

import com.noox.fitness_tracker.entity.Rol;
import com.noox.fitness_tracker.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RolSeeder implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        // Crear rol Usuario (ID 1)
        if (!rolRepository.existsByNombre("USUARIO")) {
            Rol rolUsuario = new Rol("USUARIO", "Usuario estándar del sistema");
            rolRepository.save(rolUsuario);
            System.out.println("Rol USUARIO creado con ID: " + rolUsuario.getId());
        }

        // Crear rol Admin (ID 2)
        if (!rolRepository.existsByNombre("ADMIN")) {
            Rol rolAdmin = new Rol("ADMIN", "Administrador del sistema");
            rolRepository.save(rolAdmin);
            System.out.println("Rol ADMIN creado con ID: " + rolAdmin.getId());
        }

        // Crear rol Entrenador (ID 3)
        if (!rolRepository.existsByNombre("ENTRENADOR")) {
            Rol rolEntrenador = new Rol("ENTRENADOR", "Entrenador del gimnasio");
            rolRepository.save(rolEntrenador);
            System.out.println("Rol ENTRENADOR creado con ID: " + rolEntrenador.getId());
        }

        System.out.println("Seeding de roles completado.");
    }
}
