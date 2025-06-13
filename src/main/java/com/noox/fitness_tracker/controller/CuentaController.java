package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.CuentaDTO;
import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.entity.Usuario;
import com.noox.fitness_tracker.repository.CuentaRepository;
import com.noox.fitness_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Helper method to convert Entity to DTO
    private CuentaDTO convertToDTO(Cuenta cuenta) {
        return new CuentaDTO(
                cuenta.getIdcuenta(),
                cuenta.getUsuario().getIdusuario(),
                cuenta.getCorreo(),
                null, // Do not expose password in responses
                cuenta.getCreated(),
                cuenta.getLastmodified()
        );
    }

    // Helper method to convert DTO to Entity for creation
    private Cuenta convertToEntity(CuentaDTO cuentaDTO, Usuario usuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setCorreo(cuentaDTO.getCorreo());
        cuenta.setContraseña(cuentaDTO.getContraseña());
        // created and lastmodified are set by annotations
        return cuenta;
    }

    @GetMapping
    public List<CuentaDTO> getAllCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> getCuentaById(@PathVariable Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        return cuenta.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCuenta(@RequestBody CuentaDTO cuentaDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cuentaDTO.getIdusuario());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario with id " + cuentaDTO.getIdusuario() + " not found.");
        }
        Usuario usuario = usuarioOptional.get();
        Cuenta cuenta = convertToEntity(cuentaDTO, usuario);
        try {
            Cuenta savedCuenta = cuentaRepository.save(cuenta);
            return new ResponseEntity<>(convertToDTO(savedCuenta), HttpStatus.CREATED);
        } catch (Exception e) { // Catch potential unique constraint violation for correo
             return ResponseEntity.status(HttpStatus.CONFLICT).body("Error creating cuenta: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
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
        if (cuentaDTO.getContraseña() != null && !cuentaDTO.getContraseña().isEmpty()) { // Allow password update
            existingCuenta.setContraseña(cuentaDTO.getContraseña());
        }
        // lastmodified is updated by annotation

        try {
            Cuenta updatedCuenta = cuentaRepository.save(existingCuenta);
            return ResponseEntity.ok(convertToDTO(updatedCuenta));
        } catch (Exception e) { // Catch potential unique constraint violation for correo
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error updating cuenta: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        if (cuentaRepository.existsById(id)) {
            cuentaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
