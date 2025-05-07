package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.NotificacionDTO;
import com.julian.neolearn.neolearn.entity.Notificacion;
import com.julian.neolearn.neolearn.mapper.NotificacionMapper;
import com.julian.neolearn.neolearn.repository.NotificacionRepository;
import com.julian.neolearn.neolearn.service.NotificacionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private NotificacionMapper notificacionMapper;



        @Override
    public Optional<NotificacionDTO> buscarNotificacionPorId(Long cveNotificacion) {
        return notificacionRepository.findById(cveNotificacion).map(notificacionMapper::toDTO);
    }

    @Override
    public List<NotificacionDTO> listarNotificaciones() {
        return notificacionRepository.findAll().stream().map(notificacionMapper::toDTO).toList();
    }

    public NotificacionDTO guardarNotificacion(NotificacionDTO dto) {
        Notificacion notificacion = notificacionMapper.toEntity(dto);

        if (dto.getCve_notificacion() != null) {
            // Validar existencia previa
            Optional<Notificacion> existente = notificacionRepository.findById(dto.getCve_notificacion());
            if (existente.isEmpty()) {
                throw new EntityNotFoundException("Notificacion no encontrada con ID: " + dto.getCve_notificacion());
            }
        }

        notificacion = notificacionRepository.save(notificacion);
        return notificacionMapper.toDTO(notificacion);
    }

    @Override
    public void borrarNotificacionPorId(Long cveNotificacion) {

        notificacionRepository.deleteById(cveNotificacion);
    }
}
