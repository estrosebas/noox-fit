package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.CuentaDTO;
import com.noox.fitness_tracker.dto.RegistrationForm;
import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.entity.Usuario;
import com.noox.fitness_tracker.repository.CuentaRepository;
import com.noox.fitness_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
// Removed RestController, using Controller for Thymeleaf views
// import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller // Changed from @RestController to handle Thymeleaf views
// @RequestMapping("/api/cuentas") // Keep this for API, add new one for pages or move this
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Page Endpoints ---

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Spring Security handles ?error, ?logout attributes automatically
        // registrationSuccess is handled if redirected from registration
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register"; // Return the name of the Thymeleaf template (register.html)
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("registrationForm") RegistrationForm registrationForm,
                                      BindingResult bindingResult, Model model) {
        // Basic Validation (more can be added with @Valid and annotations on DTO)
        if (registrationForm.getNombre() == null || registrationForm.getNombre().isEmpty()) {
            bindingResult.rejectValue("nombre", "error.registrationForm", "Name is required.");
        }
        if (registrationForm.getCorreo() == null || registrationForm.getCorreo().isEmpty()) {
            bindingResult.rejectValue("correo", "error.registrationForm", "Email is required.");
        } else if (cuentaRepository.findByCorreo(registrationForm.getCorreo()).isPresent()) {
            // It seems findByCorreo doesn't exist on JpaRepository by default.
            // This check needs a custom method in CuentaRepository or a different approach.
            // For now, let's assume a simple check. A better way is to add:
            // Optional<Cuenta> findByCorreo(String correo); to CuentaRepository
            // For the purpose of this step, we'll add a placeholder error if email is "test@test.com"
            // if ("test@test.com".equals(registrationForm.getCorreo())) { // Placeholder for actual DB check
            //    bindingResult.rejectValue("correo", "error.registrationForm", "Email already exists."); // Use i18n key later
            // }
            // Using the actual findByCorreo method
            if (cuentaRepository.findByCorreo(registrationForm.getCorreo()).isPresent()) {
                bindingResult.rejectValue("correo", "register.error.emailExists", "Email already exists. Please choose another one.");
            }
        }
        if (registrationForm.getPassword() == null || registrationForm.getPassword().isEmpty()) {
            bindingResult.rejectValue("password", "error.registrationForm", "Password is required.");
        }

        // Add more validation for password strength, email format, etc.

        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", registrationForm); // Send form back with errors
            return "register"; // Return to registration page with errors
        }

        // Create Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registrationForm.getNombre());
        usuario.setApellido(registrationForm.getApellido());
        usuario.setEdad(registrationForm.getEdad());
        usuario.setDireccion(registrationForm.getDireccion());
        usuario.setSexo(registrationForm.getSexo());
        usuario = usuarioRepository.save(usuario);

        // Create Cuenta
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setCorreo(registrationForm.getCorreo());
        cuenta.setContraseña(passwordEncoder.encode(registrationForm.getPassword()));
        cuentaRepository.save(cuenta);

        return "redirect:/login?registrationSuccess"; // Redirect to login with a success parameter
    }

    // --- API Endpoints for Cuentas (Kept from original, might need adjustment if base path changes) ---

    @GetMapping("/api/cuentas") // Explicitly defining API path
    @ResponseBody // Ensure response is JSON for API calls
    public List<CuentaDTO> getAllCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/cuentas/{id}") // Explicitly defining API path
    @ResponseBody // Ensure response is JSON for API calls

    public ResponseEntity<CuentaDTO> getCuentaById(@PathVariable Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        return cuenta.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST for API might conflict if /api/cuentas is not used as base for API.
    // For now, assuming this createCuenta is for API. Registration uses /register.
    @PostMapping("/api/cuentas") // Explicitly defining API path
    @ResponseBody // Ensure response is JSON for API calls
    public ResponseEntity<?> createCuentaApi(@RequestBody CuentaDTO cuentaDTO) { // Renamed to avoid clash

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cuentaDTO.getIdusuario());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario with id " + cuentaDTO.getIdusuario() + " not found.");
        }
        Usuario usuario = usuarioOptional.get();

        Cuenta cuenta = convertToEntityForApi(cuentaDTO, usuario); // Need a separate converter or adjust existing
        try {
            Cuenta savedCuenta = cuentaRepository.save(cuenta);
            return new ResponseEntity<>(convertToDTO(savedCuenta), HttpStatus.CREATED);
        } catch (Exception e) {

             return ResponseEntity.status(HttpStatus.CONFLICT).body("Error creating cuenta: " + e.getMessage());
        }
    }


    @PutMapping("/api/cuentas/{id}") // Explicitly defining API path
    @ResponseBody // Ensure response is JSON for API calls

    public ResponseEntity<?> updateCuenta(@PathVariable Long id, @RequestBody CuentaDTO cuentaDTO) {
        Optional<Cuenta> existingCuentaOptional = cuentaRepository.findById(id);
        if (existingCuentaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cuentaDTO.getIdusuario());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario with id " + cuentaDTO.getIdusuario() + " not found.");
        }

        Cuenta existingCuenta = existingCuentaOptional.get();
        Usuario usuario = usuarioOptional.get();

        existingCuenta.setUsuario(usuario);
        existingCuenta.setCorreo(cuentaDTO.getCorreo());

        if (cuentaDTO.getContraseña() != null && !cuentaDTO.getContraseña().isEmpty()) {
            // Password update for API should also be encoded if it's direct password, or handled differently
            existingCuenta.setContraseña(passwordEncoder.encode(cuentaDTO.getContraseña()));
        }
        try {
            Cuenta updatedCuenta = cuentaRepository.save(existingCuenta);
            return ResponseEntity.ok(convertToDTO(updatedCuenta));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error updating cuenta: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/cuentas/{id}") // Explicitly defining API path
    @ResponseBody // Ensure response is JSON for API calls
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        if (cuentaRepository.existsById(id)) {
            cuentaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper method to convert Entity to DTO (Used by API methods)
    private CuentaDTO convertToDTO(Cuenta cuenta) {
        return new CuentaDTO(
                cuenta.getIdcuenta(),
                cuenta.getUsuario().getIdusuario(),
                cuenta.getCorreo(),
                null, // Password not exposed
                cuenta.getCreated(),
                cuenta.getLastmodified()
        );
    }

    // Helper method to convert DTO to Entity for API creation/update
    private Cuenta convertToEntityForApi(CuentaDTO cuentaDTO, Usuario usuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setCorreo(cuentaDTO.getCorreo());
        // API DTO's password should be hashed if it's a new password being set
        if(cuentaDTO.getContraseña() != null && !cuentaDTO.getContraseña().isEmpty()){
             cuenta.setContraseña(passwordEncoder.encode(cuentaDTO.getContraseña()));
        }
        // ID and timestamps are usually handled by JPA or DB
        return cuenta;
    }
}
