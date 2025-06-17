package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.ClaseEnVivo;

@Repository
public interface ClaseEnVivoRepository extends JpaRepository<ClaseEnVivo, Long> {
    List<ClaseEnVivo> findByCurso_CveCurso(Long cursoId);
}

