package com.julian.neolearn.neolearn.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.ClaseEnVivo;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo.EstadoClase;

@Repository
public interface ClaseEnVivoRepository extends JpaRepository<ClaseEnVivo, Long> {

    List<ClaseEnVivo> findBySalaEnVivo_Curso_cveCurso(Long cursoId);

    @Query("SELECT c FROM ClaseEnVivo c WHERE c.salaEnVivo.curso.cveCurso = :cursoId AND c.fechaProgramada >= :fechaInicio AND c.fechaProgramada <= :fechaFin")
    List<ClaseEnVivo> findClasesPorCursoYRangoFecha(
            @Param("cursoId") Long cursoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT c FROM ClaseEnVivo c WHERE c.instructor.cveUsuario = :instructorId AND c.estado = :estado")
    List<ClaseEnVivo> findClasesPorInstructorYEstado(
            @Param("instructorId") Long instructorId,
            @Param("estado") EstadoClase estado);

    @Query("SELECT c FROM ClaseEnVivo c WHERE c.estado = 'EN_VIVO'")
    List<ClaseEnVivo> findClasesEnVivo();

    @Query("SELECT c FROM ClaseEnVivo c WHERE c.fechaProgramada BETWEEN :inicio AND :fin ORDER BY c.fechaProgramada")
    List<ClaseEnVivo> findClasesProgramadasEnRango(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}
