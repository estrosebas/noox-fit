package com.noox.fitness_tracker.dto;

public class EjercicioDTO {
    private Long idejercicio;
    private String nombre;
    private String descripcion;
    private String imagenurl;
    private String urlvideo;
    private String dificultad;

    public EjercicioDTO() {
    }

    public EjercicioDTO(Long idejercicio, String nombre, String descripcion, String imagenurl, String urlvideo, String dificultad) {
        this.idejercicio = idejercicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenurl = imagenurl;
        this.urlvideo = urlvideo;
        this.dificultad = dificultad;
    }

    // Getters and Setters

    public Long getIdejercicio() {
        return idejercicio;
    }

    public void setIdejercicio(Long idejercicio) {
        this.idejercicio = idejercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenurl() {
        return imagenurl;
    }

    public void setImagenurl(String imagenurl) {
        this.imagenurl = imagenurl;
    }

    public String getUrlvideo() {
        return urlvideo;
    }

    public void setUrlvideo(String urlvideo) {
        this.urlvideo = urlvideo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
}
