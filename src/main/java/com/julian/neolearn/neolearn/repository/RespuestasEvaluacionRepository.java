package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.julian.neolearn.neolearn.entity.RespuestaEvaluacion;

public interface RespuestasEvaluacionRepository extends JpaRepository<RespuestaEvaluacion, Long>{

    List<RespuestaEvaluacion> findByResultadoEvaluacion_CveResultadoEvaluacion(Long cveResultadoEvaluacion);
}
