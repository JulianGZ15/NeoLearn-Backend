package com.julian.neolearn.neolearn.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_video")
    private Long cveVideo;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String url;

    private Integer duracion_minutos;

    private Integer orden;
}
