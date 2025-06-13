package com.noox.fitness_tracker.dto;

public class RutinaDTO {
    private Long idrutina;
    private Long idcuenta;
    private String dia;
    private Long idejercicio;

    public RutinaDTO() {
    }

    public RutinaDTO(Long idrutina, Long idcuenta, String dia, Long idejercicio) {
        this.idrutina = idrutina;
        this.idcuenta = idcuenta;
        this.dia = dia;
        this.idejercicio = idejercicio;
    }

    // Getters and Setters

    public Long getIdrutina() {
        return idrutina;
    }

    public void setIdrutina(Long idrutina) {
        this.idrutina = idrutina;
    }

    public Long getIdcuenta() {
        return idcuenta;
    }

    public void setIdcuenta(Long idcuenta) {
        this.idcuenta = idcuenta;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Long getIdejercicio() {
        return idejercicio;
    }

    public void setIdejercicio(Long idejercicio) {
        this.idejercicio = idejercicio;
    }
}
