package com.julian.neolearn.neolearn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.SalaEnVivo;

@Repository
public interface SalaEnVivoRepository extends JpaRepository<SalaEnVivo, Long> {

    // ✅ Este ya está correcto
    List<SalaEnVivo> findByCurso_cveCurso(Long cursoId);

    @Query("SELECT s FROM SalaEnVivo s WHERE s.curso.cveCurso = :cursoId AND s.activa = true")
    List<SalaEnVivo> findSalasActivasPorCurso(@Param("cursoId") Long cursoId);

    Optional<SalaEnVivo> findByCodigoSala(String codigoSala);

    @Query("SELECT s FROM SalaEnVivo s JOIN FETCH s.clases c WHERE s.curso.cveCurso = :cursoId")
    List<SalaEnVivo> findSalasConClasesPorCurso(@Param("cursoId") Long cursoId);
}

