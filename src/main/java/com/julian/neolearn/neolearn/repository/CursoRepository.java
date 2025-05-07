package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Empresa;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Obtener todos los cursos de una empresa
    List<Curso> findByEmpresa(Empresa empresa);

    // Alternativa si solo tienes el ID de la empresa
    List<Curso> findByEmpresa_cveEmpresa(Long empresaId);
}

