package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Evaluacion;
import com.julian.neolearn.neolearn.entity.ResultadoEvaluacion;

@Repository
public interface ResultadoEvaluacionRepository extends JpaRepository<ResultadoEvaluacion, Long> {

    List <ResultadoEvaluacion> findByEvaluacion(Evaluacion evaluacion); 
}
