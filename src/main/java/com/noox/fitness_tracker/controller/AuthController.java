package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.model.AuthenticationResponse;
import com.noox.fitness_tracker.model.LoginRequest;
import com.noox.fitness_tracker.model.RegisterRequest;
import com.noox.fitness_tracker.service.AuthService;
import com.noox.fitness_tracker.service.CustomUserDetailsService;
import com.noox.fitness_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt for email: " + loginRequest.getCorreo());
            
            // Validate input
            if (loginRequest.getCorreo() == null || loginRequest.getCorreo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            
            if (loginRequest.getContraseña() == null || loginRequest.getContraseña().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getContraseña())
            );
            
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getCorreo());
            final String jwt = jwtUtil.generateToken(userDetails);
            
            System.out.println("Login successful for email: " + loginRequest.getCorreo());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
            
        } catch (BadCredentialsException e) {
            System.err.println("Bad credentials for email: " + loginRequest.getCorreo());
            return ResponseEntity.status(401).body("Invalid email or password");
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Validar términos
            if (!registerRequest.isTermsAccepted()) {
                return ResponseEntity.badRequest().body("error: debe aceptar los términos y condiciones");
            }
            
            // Validar datos requeridos
            if (registerRequest.getNombre() == null || registerRequest.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("error: nombre es requerido");
            }
            
            if (registerRequest.getCorreo() == null || registerRequest.getCorreo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("error: correo es requerido");
            }
            
            if (!authService.isPasswordValid(registerRequest.getContraseña())) {
                return ResponseEntity.badRequest().body("error: la contraseña debe tener al menos 6 caracteres");
            }
            
            // Intentar registrar usuario
            boolean registroExitoso = authService.registerUser(
                registerRequest.getNombre(),
                registerRequest.getApellido(),
                registerRequest.getEdad(),
                registerRequest.getDireccion(),
                registerRequest.getSexo(),
                registerRequest.getCorreo(),
                registerRequest.getContraseña()
            );
            
            if (registroExitoso) {
                System.out.println("Registro exitoso para: " + registerRequest.getCorreo());
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.badRequest().body("error: el correo ya está registrado");
            }
        } catch (Exception e) {
            System.err.println("Error en registro: " + e.getMessage());
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }
}