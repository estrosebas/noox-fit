package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.UsuarioDTO;
import com.noox.fitness_tracker.entity.Usuario;
import com.noox.fitness_tracker.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Helper method to convert Entity to DTO
    private UsuarioDTO convertToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdusuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEdad(),
                usuario.getDireccion(),
                usuario.getSexo()
        );
    }

    // Helper method to convert DTO to Entity
    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setIdusuario(usuarioDTO.getIdusuario());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEdad(usuarioDTO.getEdad());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setSexo(usuarioDTO.getSexo());
        return usuario;
    }

    @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return new ResponseEntity<>(convertToDTO(savedUsuario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Optional<Usuario> existingUsuarioOptional = usuarioRepository.findById(id);
        if (existingUsuarioOptional.isPresent()) {
            Usuario existingUsuario = existingUsuarioOptional.get();
            existingUsuario.setNombre(usuarioDTO.getNombre());
            existingUsuario.setApellido(usuarioDTO.getApellido());
            existingUsuario.setEdad(usuarioDTO.getEdad());
            existingUsuario.setDireccion(usuarioDTO.getDireccion());
            existingUsuario.setSexo(usuarioDTO.getSexo());
            Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
            return ResponseEntity.ok(convertToDTO(updatedUsuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
