package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Empresa;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Obtener todos los cursos de una empresa
    List<Curso> findByEmpresa(Empresa empresa);

    // Alternativa si solo tienes el ID de la empresa
    List<Curso> findByEmpresa_cveEmpresa(Long empresaId);

    // Cursos a los que está inscrito un usuario (estado PUBLICADO)
    @Query("""
                SELECT c FROM Curso c
                JOIN Inscripcion i ON i.curso = c
                WHERE i.usuario.cveUsuario = :usuarioId
                  AND c.estado = 'PUBLICADO'
                  AND c.empresa.cveEmpresa = :cveEmpresa

            """)
    List<Curso> findCursosInscritosPorUsuario(Long usuarioId, Long cveEmpresa);

    // Cursos PUBLICADOS que NO están inscritos por el usuario (para mostrar como
    // "comprables")
    @Query("""
                SELECT c FROM Curso c
                WHERE c.estado = 'PUBLICADO'
                    AND c.empresa.cveEmpresa = :cveEmpresa
                    AND c.id NOT IN (
                    SELECT i.curso.id FROM Inscripcion i WHERE i.usuario.cveUsuario = :usuarioId
                )
            """)
    List<Curso> findCursosDisponiblesParaCompraPorUsuario(Long usuarioId, Long cveEmpresa);

}
