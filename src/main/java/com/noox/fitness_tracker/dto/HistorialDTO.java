package com.noox.fitness_tracker.dto;

import java.time.LocalDateTime;

public class HistorialDTO {
    private Long idhistorial;
    private Long idcuenta;
    private Long idejercicio;
    private LocalDateTime lastmodified;
    private boolean hecho;

    public HistorialDTO() {
    }

    public HistorialDTO(Long idhistorial, Long idcuenta, Long idejercicio, LocalDateTime lastmodified, boolean hecho) {
        this.idhistorial = idhistorial;
        this.idcuenta = idcuenta;
        this.idejercicio = idejercicio;
        this.lastmodified = lastmodified;
        this.hecho = hecho;
    }

    // Getters and Setters

    public Long getIdhistorial() {
        return idhistorial;
    }

    public void setIdhistorial(Long idhistorial) {
        this.idhistorial = idhistorial;
    }

    public Long getIdcuenta() {
        return idcuenta;
    }

    public void setIdcuenta(Long idcuenta) {
        this.idcuenta = idcuenta;
    }

    public Long getIdejercicio() {
        return idejercicio;
    }

    public void setIdejercicio(Long idejercicio) {
        this.idejercicio = idejercicio;
    }

    public LocalDateTime getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(LocalDateTime lastmodified) {
        this.lastmodified = lastmodified;
    }

    public boolean isHecho() {
        return hecho;
    }

    public void setHecho(boolean hecho) {
        this.hecho = hecho;
    }
}
