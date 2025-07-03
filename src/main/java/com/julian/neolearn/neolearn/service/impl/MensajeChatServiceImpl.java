package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo;
import com.julian.neolearn.neolearn.entity.MensajeChat;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo.EstadoClase;
import com.julian.neolearn.neolearn.entity.MensajeChat.TipoMensaje;
import com.julian.neolearn.neolearn.mapper.MensajeChatMapper;
import com.julian.neolearn.neolearn.repository.ClaseEnVivoRepository;
import com.julian.neolearn.neolearn.repository.MensajeChatRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.requestandresponse.EnviarMensajeRequest;
import com.julian.neolearn.neolearn.requestandresponse.HistorialChatResponse;
import com.julian.neolearn.neolearn.service.MensajeChatService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MensajeChatServiceImpl implements MensajeChatService {

    private final MensajeChatRepository mensajeRepository;
    private final ClaseEnVivoRepository claseRepository;
    private final UsuarioRepository usuarioRepository;
    private final MensajeChatMapper mensajeMapper;

@Override
public MensajeChatDTO enviarMensaje(Long claseId, Long usuarioId, EnviarMensajeRequest request) {
    // ✅ VALIDAR parámetros
    if (claseId == null) {
        throw new IllegalArgumentException("claseId no puede ser null");
    }
    if (usuarioId == null) {
        throw new IllegalArgumentException("usuarioId no puede ser null");
    }
    if (request == null || request.getContenido() == null || request.getContenido().trim().isEmpty()) {
        throw new IllegalArgumentException("Contenido del mensaje no puede estar vacío");
    }

    ClaseEnVivo clase = claseRepository.findById(claseId)
        .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
    
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

    // Validar que la clase esté activa
    if (clase.getEstado() != EstadoClase.EN_VIVO) {
        throw new IllegalStateException("Solo se puede chatear en clases activas");
    }

    // Crear mensaje
    MensajeChat mensaje = new MensajeChat();
    mensaje.setClaseEnVivo(clase);
    mensaje.setUsuario(usuario);
    mensaje.setContenido(request.getContenido().trim());
    mensaje.setTipoMensaje(request.getTipoMensaje() != null ? request.getTipoMensaje() : TipoMensaje.TEXTO);

    try {
        MensajeChat mensajeGuardado = mensajeRepository.save(mensaje);
        return mensajeMapper.toDTOWithUser(mensajeGuardado, usuarioId);
    } catch (Exception e) {
        System.err.println("Error guardando mensaje: " + e.getMessage());
        throw new RuntimeException("Error al guardar el mensaje", e);
    }
}


    @Override
    @Transactional(readOnly = true)
    public HistorialChatResponse obtenerHistorial(Long claseId, int pagina, int tamaño, Long usuarioActualId) {
        Pageable pageable = PageRequest.of(pagina, tamaño);
        
        // ✅ CORREGIDO: Usar el método correcto para ID de clase
        Page<MensajeChat> mensajesPage = mensajeRepository.findByClaseEnVivo_CveClaseEnVivoOrderByTimestampDesc(claseId, pageable);
        
        List<MensajeChatDTO> mensajesDTO = mensajesPage.getContent().stream()
            .map(mensaje -> mensajeMapper.toDTOWithUser(mensaje, usuarioActualId))
            .collect(Collectors.toList());
        
        // Invertir orden para mostrar más recientes al final
        Collections.reverse(mensajesDTO);
        
        return new HistorialChatResponse(
            mensajesDTO,
            (int) mensajesPage.getTotalElements(),
            pagina,
            mensajesPage.hasNext()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialChatResponse obtenerHistorialPorCodigoSala(String codigoSala, int pagina, int tamaño, Long usuarioActualId) {
        Pageable pageable = PageRequest.of(pagina, tamaño);
        
        // ✅ CORREGIDO: Usar el método correcto para código de sala
        Page<MensajeChat> mensajesPage = mensajeRepository.findByClaseEnVivo_SalaEnVivo_CodigoSalaOrderByTimestampDesc(codigoSala, pageable);
        
        List<MensajeChatDTO> mensajesDTO = mensajesPage.getContent().stream()
            .map(mensaje -> mensajeMapper.toDTOWithUser(mensaje, usuarioActualId))
            .collect(Collectors.toList());
        
        Collections.reverse(mensajesDTO);
        
        return new HistorialChatResponse(
            mensajesDTO,
            (int) mensajesPage.getTotalElements(),
            pagina,
            mensajesPage.hasNext()
        );
    }

    @Override
    public MensajeChatDTO editarMensaje(Long mensajeId, String nuevoContenido, Long usuarioId) {
        MensajeChat mensaje = mensajeRepository.findById(mensajeId)
            .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado"));

        // Validar que el usuario sea el autor
        if (!mensaje.getUsuario().getCveUsuario().equals(usuarioId)) {
            throw new IllegalStateException("Solo puedes editar tus propios mensajes");
        }

        // Validar tiempo límite para edición (ej: 5 minutos)
        if (mensaje.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalStateException("No se puede editar mensajes después de 5 minutos");
        }

        // Validar contenido
        if (nuevoContenido == null || nuevoContenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío");
        }

        mensaje.setContenido(nuevoContenido.trim());
        mensaje.setEditado(true);
        mensaje.setFechaEdicion(LocalDateTime.now());

        MensajeChat mensajeActualizado = mensajeRepository.save(mensaje);
        return mensajeMapper.toDTOWithUser(mensajeActualizado, usuarioId);
    }

    @Override
    public void eliminarMensaje(Long mensajeId, Long usuarioId) {
        MensajeChat mensaje = mensajeRepository.findById(mensajeId)
            .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado"));

        // Validar que el usuario sea el autor o instructor
        if (!mensaje.getUsuario().getCveUsuario().equals(usuarioId)) {
            // Verificar si es instructor de la clase
            ClaseEnVivo clase = mensaje.getClaseEnVivo();
            if (!clase.getInstructor().getCveUsuario().equals(usuarioId)) {
                throw new IllegalStateException("No tienes permisos para eliminar este mensaje");
            }
        }

        mensajeRepository.delete(mensaje);
    }

    @Override
    public MensajeChatDTO enviarMensajeSistema(Long claseId, String contenido) {
        ClaseEnVivo clase = claseRepository.findById(claseId)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));

        MensajeChat mensaje = new MensajeChat();
        mensaje.setClaseEnVivo(clase);
        mensaje.setUsuario(null); // Mensaje del sistema
        mensaje.setContenido(contenido);
        mensaje.setTipoMensaje(TipoMensaje.SISTEMA);
        mensaje.setTimestamp(LocalDateTime.now());

        MensajeChat mensajeGuardado = mensajeRepository.save(mensaje);
        return mensajeMapper.toDTO(mensajeGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMensajes(Long claseId) {
        // ✅ CORREGIDO: Usar el método correcto
        return mensajeRepository.countByClaseEnVivo_CveClaseEnVivo(claseId);
    }

    @Override
    public void limpiarChatClase(Long claseId, Long usuarioId) {
        ClaseEnVivo clase = claseRepository.findById(claseId)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));

        // Solo el instructor puede limpiar el chat
        if (!clase.getInstructor().getCveUsuario().equals(usuarioId)) {
            throw new IllegalStateException("Solo el instructor puede limpiar el chat");
        }

        mensajeRepository.deleteByClaseEnVivo_CveClaseEnVivo(claseId);
    }

    // ✅ NUEVO: Método adicional para obtener mensajes recientes
    @Override
    @Transactional(readOnly = true)
    public List<MensajeChatDTO> obtenerMensajesRecientes(Long claseId, LocalDateTime desde, Long usuarioActualId) {
        List<MensajeChat> mensajes = mensajeRepository.findByClaseEnVivo_CveClaseEnVivoAndTimestampGreaterThanEqualOrderByTimestampAsc(claseId, desde);
        
        return mensajes.stream()
            .map(mensaje -> mensajeMapper.toDTOWithUser(mensaje, usuarioActualId))
            .collect(Collectors.toList());
    }

    // ✅ NUEVO: Método para validar permisos de chat
    @Override
    @Transactional(readOnly = true)
    public boolean puedeParticiparEnChat(Long claseId, Long usuarioId) {
        ClaseEnVivo clase = claseRepository.findById(claseId)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));

        // El instructor siempre puede participar
        if (clase.getInstructor().getCveUsuario().equals(usuarioId)) {
            return true;
        }

        // Solo se puede chatear en clases activas
        if (clase.getEstado() != EstadoClase.EN_VIVO) {
            return false;
        }

        // Aquí puedes agregar más lógica de validación
        // Por ejemplo, verificar si el usuario está inscrito en el curso
        return true;
    }
}
