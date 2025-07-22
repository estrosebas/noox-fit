package com.noox.fitness_tracker.service;

import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.entity.Rol;
import com.noox.fitness_tracker.entity.Usuario;
import com.noox.fitness_tracker.repository.CuentaRepository;
import com.noox.fitness_tracker.repository.RolRepository;
import com.noox.fitness_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica un usuario por correo y contraseña
     * @param correo Email del usuario
     * @param contraseña Contraseña en texto plano
     * @return true si la autenticación es exitosa, false en caso contrario
     */
    public boolean authenticate(String correo, String contraseña) {
        System.out.println("AuthService.authenticate() - Correo: " + correo);
        
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByCorreo(correo);
        
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            String hashedPassword = cuenta.getContraseña();
            
            System.out.println("AuthService.authenticate() - Cuenta encontrada, verificando contraseña...");
            System.out.println("AuthService.authenticate() - Hash length: " + (hashedPassword != null ? hashedPassword.length() : "null"));
            
            boolean matches = passwordEncoder.matches(contraseña, hashedPassword);
            System.out.println("AuthService.authenticate() - BCrypt matches result: " + matches);
            
            return matches;
        }
        
        System.out.println("AuthService.authenticate() - No se encontró cuenta para: " + correo);
        return false;
    }

    /**
     * Registra un nuevo usuario en el sistema
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param edad Edad del usuario
     * @param direccion Dirección del usuario
     * @param sexo Sexo del usuario
     * @param correo Email del usuario
     * @param contraseña Contraseña en texto plano
     * @return true si el registro es exitoso, false si el correo ya existe
     * @throws Exception Si ocurre un error durante el registro
     */
    public boolean registerUser(String nombre, String apellido, Integer edad, String direccion, 
                               String sexo, String correo, String contraseña) throws Exception {
        
        // Verificar si el correo ya existe
        if (cuentaRepository.findByCorreo(correo).isPresent()) {
            return false;
        }
        
        // Obtener el rol por defecto (USUARIO con ID 1)
        Rol rolUsuario = rolRepository.findById(1L)
            .orElseThrow(() -> new Exception("Rol USUARIO no encontrado. Ejecute el seeder primero."));
        
        // Crear y guardar usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEdad(edad);
        usuario.setDireccion(direccion);
        usuario.setSexo(sexo);
        usuario = usuarioRepository.save(usuario);
        
        // Crear y guardar cuenta con contraseña hasheada y rol por defecto
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setRol(rolUsuario);
        cuenta.setCorreo(correo);
        cuenta.setContraseña(passwordEncoder.encode(contraseña));
        cuentaRepository.save(cuenta);
        
        return true;
    }

    /**
     * Verifica si un correo ya está registrado
     * @param correo Email a verificar
     * @return true si el correo existe, false en caso contrario
     */
    public boolean emailExists(String correo) {
        return cuentaRepository.findByCorreo(correo).isPresent();
    }

    /**
     * Busca una cuenta por correo electrónico
     * @param correo Email del usuario
     * @return Optional con la cuenta si existe
     */
    public Optional<Cuenta> findByCorreo(String correo) {
        return cuentaRepository.findByCorreo(correo);
    }

    /**
     * Cambia la contraseña de un usuario
     * @param correo Email del usuario
     * @param nuevaContraseña Nueva contraseña en texto plano
     * @return true si el cambio es exitoso, false si el usuario no existe
     */
    public boolean changePassword(String correo, String nuevaContraseña) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByCorreo(correo);
        
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setContraseña(passwordEncoder.encode(nuevaContraseña));
            cuentaRepository.save(cuenta);
            return true;
        }
        
        return false;
    }

    /**
     * Valida que una contraseña cumpla con los requisitos mínimos
     * @param contraseña Contraseña a validar
     * @return true si la contraseña es válida, false en caso contrario
     */
    public boolean isPasswordValid(String contraseña) {
        return contraseña != null && contraseña.length() >= 6;
    }

    /**
     * Método de debug para verificar bcrypt - REMOVER EN PRODUCCIÓN
     * @param correo Email del usuario
     * @param contraseña Contraseña en texto plano
     * @return información de debug
     */
    public String debugAuthenticate(String correo, String contraseña) {
        System.out.println("DEBUG: Intentando autenticar correo: " + correo);
        
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByCorreo(correo);
        
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            String hashedPassword = cuenta.getContraseña();
            
            System.out.println("DEBUG: Cuenta encontrada para: " + correo);
            System.out.println("DEBUG: Hash almacenado: " + hashedPassword);
            System.out.println("DEBUG: Contraseña ingresada: " + contraseña);
            System.out.println("DEBUG: Rol: " + cuenta.getRol().getNombre());
            
            boolean matches = passwordEncoder.matches(contraseña, hashedPassword);
            System.out.println("DEBUG: BCrypt matches resultado: " + matches);
            
            return "Cuenta encontrada: " + (cuenta != null) + 
                   ", Hash: " + (hashedPassword != null && hashedPassword.length() > 0) + 
                   ", Rol: " + cuenta.getRol().getNombre() +
                   ", Matches: " + matches;
        } else {
            System.out.println("DEBUG: No se encontró cuenta para: " + correo);
            return "Cuenta no encontrada para: " + correo;
        }
    }

    /**
     * Obtiene el rol de un usuario por su correo
     * @param correo Email del usuario
     * @return Optional con el rol si existe
     */
    public Optional<Rol> getUserRole(String correo) {
        return cuentaRepository.findByCorreo(correo)
                .map(Cuenta::getRol);
    }

    /**
     * Verifica si un usuario tiene un rol específico
     * @param correo Email del usuario
     * @param rolNombre Nombre del rol a verificar
     * @return true si el usuario tiene el rol, false en caso contrario
     */
    public boolean hasRole(String correo, String rolNombre) {
        return getUserRole(correo)
                .map(rol -> rol.getNombre().equals(rolNombre))
                .orElse(false);
    }

    /**
     * Cambia el rol de un usuario
     * @param correo Email del usuario
     * @param nuevoRolId ID del nuevo rol
     * @return true si el cambio es exitoso, false si el usuario o rol no existe
     */
    public boolean changeUserRole(String correo, Long nuevoRolId) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByCorreo(correo);
        Optional<Rol> rolOpt = rolRepository.findById(nuevoRolId);
        
        if (cuentaOpt.isPresent() && rolOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setRol(rolOpt.get());
            cuentaRepository.save(cuenta);
            return true;
        }
        
        return false;
    }
}
