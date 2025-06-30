package com.julian.neolearn.neolearn.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "configuracion_certificado")
public class ConfiguracionCertificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveConfiguracion;

    private String firma;
    private String logo;
    private String firmante;

    @ManyToOne
    @JoinColumn(name = "cve_empresa")
    private Empresa empresa;
}
