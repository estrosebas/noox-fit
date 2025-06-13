package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.EjercicioDTO;
import com.noox.fitness_tracker.entity.Ejercicio;
import com.noox.fitness_tracker.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    // Helper method to convert Entity to DTO
    private EjercicioDTO convertToDTO(Ejercicio ejercicio) {
        return new EjercicioDTO(
                ejercicio.getIdejercicio(),
                ejercicio.getNombre(),
                ejercicio.getDescripcion(),
                ejercicio.getImagenurl(),
                ejercicio.getUrlvideo(),
                ejercicio.getDificultad()
        );
    }

    // Helper method to convert DTO to Entity
    private Ejercicio convertToEntity(EjercicioDTO ejercicioDTO) {
        Ejercicio ejercicio = new Ejercicio();
        // For updates, id might be needed, but for creates it's generated.
        // Let's assume DTO's id is ignored for create, and used for update.
        if (ejercicioDTO.getIdejercicio() != null) {
            ejercicio.setIdejercicio(ejercicioDTO.getIdejercicio());
        }
        ejercicio.setNombre(ejercicioDTO.getNombre());
        ejercicio.setDescripcion(ejercicioDTO.getDescripcion());
        ejercicio.setImagenurl(ejercicioDTO.getImagenurl());
        ejercicio.setUrlvideo(ejercicioDTO.getUrlvideo());
        ejercicio.setDificultad(ejercicioDTO.getDificultad());
        return ejercicio;
    }

    @GetMapping
    public List<EjercicioDTO> getAllEjercicios() {
        return ejercicioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjercicioDTO> getEjercicioById(@PathVariable Long id) {
        Optional<Ejercicio> ejercicio = ejercicioRepository.findById(id);
        return ejercicio.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EjercicioDTO> createEjercicio(@RequestBody EjercicioDTO ejercicioDTO) {
        Ejercicio ejercicio = convertToEntity(ejercicioDTO);
        // Ensure idejercicio is null for creation so it's auto-generated
        ejercicio.setIdejercicio(null);
        try {
            Ejercicio savedEjercicio = ejercicioRepository.save(ejercicio);
            return new ResponseEntity<>(convertToDTO(savedEjercicio), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a more specific error DTO
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EjercicioDTO> updateEjercicio(@PathVariable Long id, @RequestBody EjercicioDTO ejercicioDTO) {
        Optional<Ejercicio> existingEjercicioOptional = ejercicioRepository.findById(id);
        if (existingEjercicioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ejercicio existingEjercicio = existingEjercicioOptional.get();
        // Update fields from DTO
        existingEjercicio.setNombre(ejercicioDTO.getNombre());
        existingEjercicio.setDescripcion(ejercicioDTO.getDescripcion());
        existingEjercicio.setImagenurl(ejercicioDTO.getImagenurl());
        existingEjercicio.setUrlvideo(ejercicioDTO.getUrlvideo());
        existingEjercicio.setDificultad(ejercicioDTO.getDificultad());

        try {
            Ejercicio updatedEjercicio = ejercicioRepository.save(existingEjercicio);
            return ResponseEntity.ok(convertToDTO(updatedEjercicio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or a more specific error DTO
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjercicio(@PathVariable Long id) {
        if (ejercicioRepository.existsById(id)) {
            ejercicioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
