package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.entity.MensajeChat;
import com.julian.neolearn.neolearn.mapper.MensajeChatMapper;
import com.julian.neolearn.neolearn.repository.MensajeChatRepository;
import com.julian.neolearn.neolearn.service.MensajeChatService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MensajeChatServiceImpl implements MensajeChatService {

    @Autowired
    private MensajeChatRepository mensajeChatRepository;
    @Autowired
    private MensajeChatMapper mensajeChatMapper;



        @Override
    public Optional<MensajeChatDTO> buscarMensajeChatPorId(Long cveMensajeChat) {
        return mensajeChatRepository.findById(cveMensajeChat).map(mensajeChatMapper::toDTO);
    }

    @Override
    public List<MensajeChatDTO> listarMensajesChat() {
        return mensajeChatRepository.findAll().stream().map(mensajeChatMapper::toDTO).toList();
    }

    public MensajeChatDTO guardarMensajeChat(MensajeChatDTO dto) {
        MensajeChat mensajeChat = mensajeChatMapper.toEntity(dto);

        if (dto.getCve_mensajeChat() != null) {
            // Validar existencia previa
            Optional<MensajeChat> existente = mensajeChatRepository.findById(dto.getCve_mensajeChat());
            if (existente.isEmpty()) {
                throw new EntityNotFoundException("MensajeChat no encontrada con ID: " + dto.getCve_mensajeChat());
            }
        }

        mensajeChat = mensajeChatRepository.save(mensajeChat);
        return mensajeChatMapper.toDTO(mensajeChat);
    }

    @Override
    public void borrarMensajeChatPorId(Long cveMensajeChat) {

        mensajeChatRepository.deleteById(cveMensajeChat);
    }
    
}
