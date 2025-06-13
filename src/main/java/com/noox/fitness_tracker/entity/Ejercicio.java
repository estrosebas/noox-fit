package com.noox.fitness_tracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ejercicios")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idejercicio;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenurl;

    @Column(name = "url_video")
    private String urlvideo;

    @Column
    private String dificultad;

    public Ejercicio() {
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
