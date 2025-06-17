package com.julian.neolearn.neolearn.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Certificado;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
 List<Certificado> findByInscripcion_CveInscripcion(Long cveInscripcion);

}
