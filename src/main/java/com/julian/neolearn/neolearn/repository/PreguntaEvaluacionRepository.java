package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.PreguntaEvaluacion;

@Repository
public interface PreguntaEvaluacionRepository extends JpaRepository<PreguntaEvaluacion, Long> {

    List<PreguntaEvaluacion> findByEvaluacion_CveEvaluacion(Long cveEvaluacion);
}
