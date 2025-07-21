package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.EjercicioDTO;
import com.noox.fitness_tracker.entity.Ejercicio;
import com.noox.fitness_tracker.entity.Rutina;
import com.noox.fitness_tracker.repository.EjercicioRepository;
import com.noox.fitness_tracker.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

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

    @GetMapping("/con-rutinas")
    public ResponseEntity<List<Map<String, Object>>> getEjerciciosConRutinas() {
        try {
            List<Rutina> rutinas = rutinaRepository.findAll();
            List<Map<String, Object>> response = rutinas.stream().map(rutina -> {
                Map<String, Object> ejercicioMap = new HashMap<>();
                Ejercicio ejercicio = rutina.getEjercicio();
                
                ejercicioMap.put("idejercicio", ejercicio.getIdejercicio());
                ejercicioMap.put("nombre", ejercicio.getNombre());
                ejercicioMap.put("descripcion", ejercicio.getDescripcion());
                ejercicioMap.put("imagenurl", ejercicio.getImagenurl());
                ejercicioMap.put("urlvideo", ejercicio.getUrlvideo());
                ejercicioMap.put("dificultad", ejercicio.getDificultad());
                // Datos de la rutina
                ejercicioMap.put("dia", rutina.getDia());
                ejercicioMap.put("idrutina", rutina.getIdrutina());
                ejercicioMap.put("hecho", false); // Por defecto no está hecho
                
                return ejercicioMap;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rutinas/dia/{dia}")
    public ResponseEntity<List<Map<String, Object>>> getEjerciciosPorDia(@PathVariable String dia) {
        try {
            List<Rutina> rutinas = rutinaRepository.findByDia(dia);
            List<Map<String, Object>> response = rutinas.stream().map(rutina -> {
                Map<String, Object> ejercicioMap = new HashMap<>();
                Ejercicio ejercicio = rutina.getEjercicio();
                
                ejercicioMap.put("idejercicio", ejercicio.getIdejercicio());
                ejercicioMap.put("nombre", ejercicio.getNombre());
                ejercicioMap.put("descripcion", ejercicio.getDescripcion());
                ejercicioMap.put("imagenurl", ejercicio.getImagenurl());
                ejercicioMap.put("urlvideo", ejercicio.getUrlvideo());
                ejercicioMap.put("dificultad", ejercicio.getDificultad());
                // Datos de la rutina
                ejercicioMap.put("dia", rutina.getDia());
                ejercicioMap.put("idrutina", rutina.getIdrutina());
                ejercicioMap.put("hecho", false); // Por defecto no está hecho
                
                return ejercicioMap;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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

    @GetMapping("/rutinas/todos-los-dias")
    public ResponseEntity<Map<String, Object>> getEjerciciosTodosLosDias() {
        try {
            List<Rutina> todasLasRutinas = rutinaRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            
            // Agrupar por días
            String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
            
            for (String dia : diasSemana) {
                List<Rutina> rutinasDia = todasLasRutinas.stream()
                    .filter(r -> r.getDia().equals(dia))
                    .collect(Collectors.toList());
                
                List<Map<String, Object>> ejerciciosDia = rutinasDia.stream().map(rutina -> {
                    Map<String, Object> ejercicioMap = new HashMap<>();
                    Ejercicio ejercicio = rutina.getEjercicio();
                    
                    ejercicioMap.put("idejercicio", ejercicio.getIdejercicio());
                    ejercicioMap.put("nombre", ejercicio.getNombre());
                    ejercicioMap.put("descripcion", ejercicio.getDescripcion());
                    ejercicioMap.put("imagenurl", ejercicio.getImagenurl());
                    ejercicioMap.put("urlvideo", ejercicio.getUrlvideo());
                    ejercicioMap.put("dificultad", ejercicio.getDificultad());
                    ejercicioMap.put("dia", rutina.getDia());
                    ejercicioMap.put("idrutina", rutina.getIdrutina());
                    ejercicioMap.put("hecho", false);
                    
                    return ejercicioMap;
                }).collect(Collectors.toList());
                
                response.put(dia, ejerciciosDia);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
