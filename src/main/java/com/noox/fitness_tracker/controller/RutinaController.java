package com.noox.fitness_tracker.controller;

import com.noox.fitness_tracker.dto.RutinaDTO;
import com.noox.fitness_tracker.entity.Cuenta;
import com.noox.fitness_tracker.entity.Ejercicio;
import com.noox.fitness_tracker.entity.Rutina;
import com.noox.fitness_tracker.repository.CuentaRepository;
import com.noox.fitness_tracker.repository.EjercicioRepository;
import com.noox.fitness_tracker.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    // Helper method to convert Entity to DTO
    private RutinaDTO convertToDTO(Rutina rutina) {
        return new RutinaDTO(
                rutina.getIdrutina(),
                rutina.getCuenta().getIdcuenta(),
                rutina.getDia(),
                rutina.getEjercicio().getIdejercicio()
        );
    }

    @GetMapping
    public List<RutinaDTO> getAllRutinas(
            @RequestParam(required = false) Long idcuenta,
            @RequestParam(required = false) String dia) {

        List<Rutina> rutinas;
        if (idcuenta != null && dia != null) {
            rutinas = rutinaRepository.findByCuentaIdcuentaAndDia(idcuenta, dia);
        } else if (idcuenta != null) {
            rutinas = rutinaRepository.findByCuentaIdcuenta(idcuenta);
        } else if (dia != null) {
            rutinas = rutinaRepository.findByDia(dia);
        } else {
            rutinas = rutinaRepository.findAll();
        }
        return rutinas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaDTO> getRutinaById(@PathVariable Long id) {
        Optional<Rutina> rutina = rutinaRepository.findById(id);
        return rutina.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createRutina(@RequestBody RutinaDTO rutinaDTO) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(rutinaDTO.getIdcuenta());
        if (cuentaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cuenta with id " + rutinaDTO.getIdcuenta() + " not found.");
        }

        Optional<Ejercicio> ejercicioOptional = ejercicioRepository.findById(rutinaDTO.getIdejercicio());
        if (ejercicioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ejercicio with id " + rutinaDTO.getIdejercicio() + " not found.");
        }

        Rutina rutina = new Rutina();
        rutina.setCuenta(cuentaOptional.get());
        rutina.setEjercicio(ejercicioOptional.get());
        rutina.setDia(rutinaDTO.getDia());

        try {
            Rutina savedRutina = rutinaRepository.save(rutina);
            return new ResponseEntity<>(convertToDTO(savedRutina), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rutina: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRutina(@PathVariable Long id, @RequestBody RutinaDTO rutinaDTO) {
        Optional<Rutina> existingRutinaOptional = rutinaRepository.findById(id);
        if (existingRutinaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(rutinaDTO.getIdcuenta());
        if (cuentaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cuenta with id " + rutinaDTO.getIdcuenta() + " not found.");
        }

        Optional<Ejercicio> ejercicioOptional = ejercicioRepository.findById(rutinaDTO.getIdejercicio());
        if (ejercicioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ejercicio with id " + rutinaDTO.getIdejercicio() + " not found.");
        }

        Rutina existingRutina = existingRutinaOptional.get();
        existingRutina.setCuenta(cuentaOptional.get());
        existingRutina.setEjercicio(ejercicioOptional.get());
        existingRutina.setDia(rutinaDTO.getDia());

        try {
            Rutina updatedRutina = rutinaRepository.save(existingRutina);
            return ResponseEntity.ok(convertToDTO(updatedRutina));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating rutina: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRutina(@PathVariable Long id) {
        if (rutinaRepository.existsById(id)) {
            rutinaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
