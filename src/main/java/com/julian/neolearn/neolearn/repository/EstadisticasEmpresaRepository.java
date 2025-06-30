package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.julian.neolearn.neolearn.entity.Usuario;

@Repository
public interface EstadisticasEmpresaRepository extends JpaRepository<Usuario, Long> {

    // Número de cursos de la empresa
    @Query(value = "SELECT COUNT(*) FROM curso WHERE cve_empresa = :cveEmpresa", nativeQuery = true)
    Long contarCursosPorEmpresa(@Param("cveEmpresa") Long cveEmpresa);

    // Ganancias en el mes actual
    @Query(value = "SELECT COALESCE(SUM(i.precio_pagado), 0) FROM inscripcion i " +
                   "JOIN curso c ON i.cve_curso = c.cve_curso " +
                   "WHERE c.cve_empresa = :cveEmpresa " +
                   "AND YEAR(i.fecha_inscripcion) = YEAR(CURDATE()) " +
                   "AND MONTH(i.fecha_inscripcion) = MONTH(CURDATE())", nativeQuery = true)
    Double calcularGananciasMesActual(@Param("cveEmpresa") Long cveEmpresa);

    // Total de suscripciones de todos los cursos de la empresa
    @Query(value = "SELECT COUNT(*) FROM inscripcion i " +
                   "JOIN curso c ON i.cve_curso = c.cve_curso " +
                   "WHERE c.cve_empresa = :cveEmpresa", nativeQuery = true)
    Long contarTotalSuscripciones(@Param("cveEmpresa") Long cveEmpresa);

    // Total de estudiantes (usuarios finales) de la empresa
    @Query(value = "SELECT COUNT(DISTINCT u.cve_usuario) FROM usuario u " +
                   "JOIN inscripcion i ON u.cve_usuario = i.cve_usuario " +
                   "JOIN curso c ON i.cve_curso = c.cve_curso " +
                   "WHERE c.cve_empresa = :cveEmpresa AND u.tipo = 'FINAL'", nativeQuery = true)
    Long contarEstudiantesEmpresa(@Param("cveEmpresa") Long cveEmpresa);

    // Últimos 5 suscriptores
@Query(value = "SELECT u.cve_usuario, u.nombre, u.correo FROM usuario u " +
               "JOIN inscripcion i ON u.cve_usuario = i.cve_usuario " +
               "JOIN curso c ON i.cve_curso = c.cve_curso " +
               "WHERE c.cve_empresa = ?1 " +
               "ORDER BY i.fecha_inscripcion DESC " +
               "LIMIT 5", nativeQuery = true)
List<Object[]> encontrarUltimosCincoSuscriptores(Long cveEmpresa);

    // Top cursos más vendidos
    @Query(value = "SELECT c.cve_curso, c.titulo, COUNT(i.cve_inscripcion) AS total_inscripciones, SUM(i.precio_pagado) AS total_ganado " +
                   "FROM curso c " +
                   "JOIN inscripcion i ON c.cve_curso = i.cve_curso " +
                   "WHERE c.cve_empresa = :cveEmpresa " +
                   "GROUP BY c.cve_curso, c.titulo " +
                   "ORDER BY total_inscripciones DESC " +
                   "LIMIT 5", nativeQuery = true)
    List<Object[]> encontrarCursosMasVendidos(@Param("cveEmpresa") Long cveEmpresa);

    // Suscripciones por mes
    @Query(value = "SELECT YEAR(i.fecha_inscripcion) AS anio, MONTH(i.fecha_inscripcion) AS mes, COUNT(*) AS total " +
                   "FROM inscripcion i " +
                   "JOIN curso c ON i.cve_curso = c.cve_curso " +
                   "WHERE c.cve_empresa = :cveEmpresa " +
                   "GROUP BY anio, mes " +
                   "ORDER BY anio DESC, mes DESC", nativeQuery = true)
    List<Object[]> obtenerSuscripcionesPorMes(@Param("cveEmpresa") Long cveEmpresa);
}

