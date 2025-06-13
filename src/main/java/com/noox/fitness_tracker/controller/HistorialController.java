package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.HistorialDTO;
import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.entity.Ejercicio;
import com.noox.fitness_tracker.entity.Historial;
import com.noox.fitness_tracker.repository.CuentaRepository;
import com.noox.fitness_tracker.repository.EjercicioRepository;
import com.noox.fitness_tracker.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historiales")
public class HistorialController {

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    // Helper method to convert Entity to DTO
    private HistorialDTO convertToDTO(Historial historial) {
        return new HistorialDTO(
                historial.getIdhistorial(),
                historial.getCuenta().getIdcuenta(),
                historial.getEjercicio().getIdejercicio(),
                historial.getLastmodified(),
                historial.isHecho()
        );
    }

    @GetMapping
    public List<HistorialDTO> getAllHistoriales(
            @RequestParam(required = false) Long idcuenta,
            @RequestParam(required = false) Long idejercicio,
            @RequestParam(required = false) Boolean hecho) {

        List<Historial> historiales;
        if (idcuenta != null && hecho != null) {
            historiales = historialRepository.findByCuentaIdcuentaAndHecho(idcuenta, hecho);
        } else if (idcuenta != null) {
            historiales = historialRepository.findByCuentaIdcuenta(idcuenta);
        } else if (idejercicio != null) {
            // Consider if filtering by idejercicio and hecho is needed
            historiales = historialRepository.findByEjercicioIdejercicio(idejercicio);
        } else if (hecho != null) {
            historiales = historialRepository.findByHecho(hecho);
        } else {
            historiales = historialRepository.findAll();
        }
        return historiales.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialDTO> getHistorialById(@PathVariable Long id) {
        Optional<Historial> historial = historialRepository.findById(id);
        return historial.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createHistorial(@RequestBody HistorialDTO historialDTO) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(historialDTO.getIdcuenta());
        if (cuentaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cuenta with id " + historialDTO.getIdcuenta() + " not found.");
        }

        Optional<Ejercicio> ejercicioOptional = ejercicioRepository.findById(historialDTO.getIdejercicio());
        if (ejercicioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ejercicio with id " + historialDTO.getIdejercicio() + " not found.");
        }

        Historial historial = new Historial();
        historial.setCuenta(cuentaOptional.get());
        historial.setEjercicio(ejercicioOptional.get());
        historial.setHecho(historialDTO.isHecho());
        // lastmodified is set by @UpdateTimestamp or @CreationTimestamp

        try {
            Historial savedHistorial = historialRepository.save(historial);
            return new ResponseEntity<>(convertToDTO(savedHistorial), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating historial: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHistorial(@PathVariable Long id, @RequestBody HistorialDTO historialDTO) {
        Optional<Historial> existingHistorialOptional = historialRepository.findById(id);
        if (existingHistorialOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(historialDTO.getIdcuenta());
        if (cuentaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cuenta with id " + historialDTO.getIdcuenta() + " not found.");
        }

        Optional<Ejercicio> ejercicioOptional = ejercicioRepository.findById(historialDTO.getIdejercicio());
        if (ejercicioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ejercicio with id " + historialDTO.getIdejercicio() + " not found.");
        }

        Historial existingHistorial = existingHistorialOptional.get();
        existingHistorial.setCuenta(cuentaOptional.get());
        existingHistorial.setEjercicio(ejercicioOptional.get());
        existingHistorial.setHecho(historialDTO.isHecho());
        // lastmodified is updated by @UpdateTimestamp

        try {
            Historial updatedHistorial = historialRepository.save(existingHistorial);
            return ResponseEntity.ok(convertToDTO(updatedHistorial));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating historial: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorial(@PathVariable Long id) {
        if (historialRepository.existsById(id)) {
            historialRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
