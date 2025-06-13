package com.noox.fitness_tracker.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "historiales")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idhistorial;

    @ManyToOne
    @JoinColumn(name = "idcuenta", nullable = false)
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "idejercicio", nullable = false)
    private Ejercicio ejercicio;

    @UpdateTimestamp
    private LocalDateTime lastmodified;

    @Column(nullable = false)
    private boolean hecho;

    public Historial() {
    }

    // Getters and Setters

    public Long getIdhistorial() {
        return idhistorial;
    }

    public void setIdhistorial(Long idhistorial) {
        this.idhistorial = idhistorial;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
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
