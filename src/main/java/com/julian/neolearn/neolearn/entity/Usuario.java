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
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_usuario")
    private Long cveUsuario;

    private String nombre;

    @Column(unique = true)
    private String correo;

    private String contrasena;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @ManyToMany(mappedBy = "usuarios")
    private List<Empresa> empresas = new ArrayList<>();

    
    

    private LocalDate fecha_registro;

    public enum TipoUsuario {
        EMPRESARIAL,
        FINAL
    }

    @PrePersist
    public void prePersist() {
        this.fecha_registro = LocalDate.now();
    }
}
