package com.julian.neolearn.neolearn.entity;

import java.time.LocalDateTime;
import java.util.Map;

public class SalaEnVivo {
    private String roomId;
    private Long claseId;
    private String instructor; // Session ID del instructor
    private Map<String, LocalDateTime> estudiantes; // Session ID -> Fecha conexión
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean activa;
    private int maxParticipantes = 50; // Límite de participantes
    
    // Constructors, getters y setters
    
    public boolean puedeUnirse() {
        return activa && estudiantes.size() < maxParticipantes;
    }
    
    public int getTotalParticipantes() {
        int total = estudiantes.size();
        if (instructor != null) total++;
        return total;
    }
}
