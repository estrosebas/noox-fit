package com.noox.fitness_tracker.repository;

import com.noox.fitness_tracker.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    List<Rutina> findByCuentaIdcuenta(Long idcuenta);
    List<Rutina> findByDia(String dia);
    List<Rutina> findByCuentaIdcuentaAndDia(Long idcuenta, String dia);
}
