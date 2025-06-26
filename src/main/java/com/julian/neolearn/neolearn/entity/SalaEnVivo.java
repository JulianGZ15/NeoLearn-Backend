package com.julian.neolearn.neolearn.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sala_en_vivo")
public class SalaEnVivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_salaEnVivo")
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoSala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private Boolean activa = false;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "salaEnVivo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "cve_claseEnVivo")
    private List<ClaseEnVivo> clases = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "sala_participantes", joinColumns = @JoinColumn(name = "sala_id"))
    @Column(name = "usuario_id")
    private List<Long> participantes = new ArrayList<>();

    // Getters y setters
}
