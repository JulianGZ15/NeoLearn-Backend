package com.julian.neolearn.neolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.ReporteCurso;

@Repository
public interface ReporteCursoRepository extends JpaRepository<ReporteCurso, Long> {
}
