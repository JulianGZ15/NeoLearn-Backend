package com.julian.neolearn.neolearn.dto;


public class CursoVendidoDTO {

     private Long cveCurso;
    private String titulo;
    private Long totalInscripciones;
    private Double totalIngresos;

    public CursoVendidoDTO(Long cveCurso, String titulo, Long totalInscripciones, Double totalIngresos) {
        this.cveCurso = cveCurso;
        this.titulo = titulo;
        this.totalInscripciones = totalInscripciones;
        this.totalIngresos = totalIngresos;
    }

    public Long getCveCurso() {
        return cveCurso;
    }

    public void setCveCurso(Long cveCurso) {
        this.cveCurso = cveCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getTotalInscripciones() {
        return totalInscripciones;
    }

    public void setTotalInscripciones(Long totalInscripciones) {
        this.totalInscripciones = totalInscripciones;
    }

    public Double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(Double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    
}
