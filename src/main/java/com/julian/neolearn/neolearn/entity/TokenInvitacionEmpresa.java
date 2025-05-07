package com.julian.neolearn.neolearn.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token_invitacion_empresa")
public class TokenInvitacionEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_token")
    private Long cveToken;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cve_empresa", nullable = false)
    private Empresa empresa;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    private boolean usado = false;

    // Getters y Setters
}
