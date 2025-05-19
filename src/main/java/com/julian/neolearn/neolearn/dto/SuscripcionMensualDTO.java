package com.julian.neolearn.neolearn.dto;


public class SuscripcionMensualDTO {
 private Integer anio;
    private Integer mes;
    private Long totalSuscripciones;

        public SuscripcionMensualDTO(Integer anio, Integer mes, Long totalSuscripciones) {
        this.anio = anio;
        this.mes = mes;
        this.totalSuscripciones = totalSuscripciones;
    }

        public Integer getAnio() {
            return anio;
        }

        public void setAnio(Integer anio) {
            this.anio = anio;
        }

        public Integer getMes() {
            return mes;
        }

        public void setMes(Integer mes) {
            this.mes = mes;
        }

        public Long getTotalSuscripciones() {
            return totalSuscripciones;
        }

        public void setTotalSuscripciones(Long totalSuscripciones) {
            this.totalSuscripciones = totalSuscripciones;
        }

    
}
