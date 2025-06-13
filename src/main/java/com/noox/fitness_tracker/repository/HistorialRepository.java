package com.noox.fitness_tracker.repository;

import com.noox.fitness_tracker.entity.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {
    List<Historial> findByCuentaIdcuenta(Long idcuenta);
    List<Historial> findByEjercicioIdejercicio(Long idejercicio);
    List<Historial> findByHecho(boolean hecho);
    List<Historial> findByCuentaIdcuentaAndHecho(Long idcuenta, boolean hecho);
}
