package com.noox.fitness_tracker.dto;

public class UsuarioDTO {
    private Long idusuario;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String direccion;
    private String sexo;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long idusuario, String nombre, String apellido, Integer edad, String direccion, String sexo) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.direccion = direccion;
        this.sexo = sexo;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
