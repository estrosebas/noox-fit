package com.noox.fitness_tracker.dto;

import java.time.LocalDateTime;

public class CuentaDTO {
    private Long idcuenta;
    private Long idusuario; // To represent the foreign key
    private String correo;
    private String contraseña; // Consider security implications
    private LocalDateTime created;
    private LocalDateTime lastmodified;

    public CuentaDTO() {
    }

    public CuentaDTO(Long idcuenta, Long idusuario, String correo, String contraseña, LocalDateTime created, LocalDateTime lastmodified) {
        this.idcuenta = idcuenta;
        this.idusuario = idusuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.created = created;
        this.lastmodified = lastmodified;
    }

    // Getters and Setters

    public Long getIdcuenta() {
        return idcuenta;
    }

    public void setIdcuenta(Long idcuenta) {
        this.idcuenta = idcuenta;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(LocalDateTime lastmodified) {
        this.lastmodified = lastmodified;
    }
}
