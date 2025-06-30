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

    private String fotoperfil;

    private Long telefono;

    // Campos de dirección
    private String direccionCompleta;
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    private String codigoPostal;
    private String pais;
    
    // Coordenadas geográficas
    private Double latitud;
    private Double longitud;
    
    // Identificadores de Google
    private String placeId;
    
    @Column(columnDefinition = "TEXT")
    private String googleAddressComponents;

    @ManyToMany(mappedBy = "usuarios")
    private List<Empresa> empresas = new ArrayList<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaseEnVivo> clasesEnVivo = new ArrayList<>();
    
    

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
