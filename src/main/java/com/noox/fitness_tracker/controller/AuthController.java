package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import com.noox.fitness_tracker.model.LoginRequest;
import com.noox.fitness_tracker.model.RegisterRequest;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/auth/login")
    @ResponseBody
    public String login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar datos de entrada
            if (loginRequest.getCorreo() == null || loginRequest.getCorreo().trim().isEmpty()) {
                return "error: correo es requerido";
            }
            
            if (loginRequest.getContraseña() == null || loginRequest.getContraseña().trim().isEmpty()) {
                return "error: contraseña es requerida";
            }
            
            // Autenticar usuario
            if (authService.authenticate(loginRequest.getCorreo(), loginRequest.getContraseña())) {
                System.out.println("Login exitoso para: " + loginRequest.getCorreo());
                return "success";
            } else {
                System.out.println("Login fallido para: " + loginRequest.getCorreo());
                return "error: credenciales incorrectas";
            }
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/api/auth/debug")
    @ResponseBody
    public String debugLogin(@RequestBody LoginRequest loginRequest) {
        try {
            String result = authService.debugAuthenticate(loginRequest.getCorreo(), loginRequest.getContraseña());
            return result;
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public String register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Validar términos
            if (!registerRequest.isTermsAccepted()) {
                return "error: debe aceptar los términos y condiciones";
            }
            
            // Validar datos requeridos
            if (registerRequest.getNombre() == null || registerRequest.getNombre().trim().isEmpty()) {
                return "error: nombre es requerido";
            }
            
            if (registerRequest.getCorreo() == null || registerRequest.getCorreo().trim().isEmpty()) {
                return "error: correo es requerido";
            }
            
            if (!authService.isPasswordValid(registerRequest.getContraseña())) {
                return "error: la contraseña debe tener al menos 6 caracteres";
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
                return "success";
            } else {
                return "error: el correo ya está registrado";
            }
        } catch (Exception e) {
            System.err.println("Error en registro: " + e.getMessage());
            return "error: " + e.getMessage();
        }
    }
}