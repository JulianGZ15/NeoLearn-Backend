package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.NotificacionDTO;
public interface NotificacionService {

    Optional<NotificacionDTO> buscarNotificacionPorId(Long cveNotificacion);
    List<NotificacionDTO> listarNotificaciones();
    NotificacionDTO guardarNotificacion(NotificacionDTO notificacion);
    void borrarNotificacionPorId(Long cveNotificacion);
    
}
