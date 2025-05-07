package com.julian.neolearn.neolearn.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_empresa")
    private Long cveEmpresa;

    @ManyToMany
    @JoinTable(
        name = "usuarios_empresa",
        joinColumns = @JoinColumn(name = "cve_empresa"),
        inverseJoinColumns = @JoinColumn(name = "cve_usuario")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    
    
    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoPlan tipo_plan;

    private LocalDate fecha_inicio_plan;

    private LocalDate fecha_fin_plan;

    private Boolean esta_activo;

    public enum TipoPlan {
        PRIVADO,
        VENTA_PUBLICA
    }
}
