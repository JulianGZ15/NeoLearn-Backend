package com.julian.neolearn.neolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.julian.neolearn.neolearn.entity.ConfiguracionCertificado;

public interface ConfiguracionRepository extends JpaRepository<ConfiguracionCertificado, Long> {
    
    ConfiguracionCertificado findByEmpresa_CveEmpresa(Long cveEmpresa);
}
