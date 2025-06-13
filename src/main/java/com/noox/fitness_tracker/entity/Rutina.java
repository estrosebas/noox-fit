package com.noox.fitness_tracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrutina;

    @ManyToOne
    @JoinColumn(name = "idcuenta", nullable = false)
    private Cuenta cuenta;

    @Column(nullable = false)
    private String dia; // e.g., "Lunes", "Martes"

    @ManyToOne
    @JoinColumn(name = "idejercicio", nullable = false)
    private Ejercicio ejercicio;

    public Rutina() {
    }

    // Getters and Setters

    public Long getIdrutina() {
        return idrutina;
    }

    public void setIdrutina(Long idrutina) {
        this.idrutina = idrutina;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }
}
