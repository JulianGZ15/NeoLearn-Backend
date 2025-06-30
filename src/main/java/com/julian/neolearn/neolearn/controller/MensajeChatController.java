package com.julian.neolearn.neolearn.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.requestandresponse.EnviarMensajeRequest;
import com.julian.neolearn.neolearn.requestandresponse.HistorialChatResponse;
import com.julian.neolearn.neolearn.service.MensajeChatService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class MensajeChatController {

    private final MensajeChatService chatService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/clase/{claseId}/mensaje")
    public ResponseEntity<MensajeChatDTO> enviarMensaje(
            @PathVariable Long claseId,
            @RequestBody EnviarMensajeRequest request,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        MensajeChatDTO mensaje = chatService.enviarMensaje(claseId, usuarioId, request);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/clase/{claseId}/historial")
    public ResponseEntity<HistorialChatResponse> obtenerHistorial(
            @PathVariable Long claseId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamaño,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        HistorialChatResponse historial = chatService.obtenerHistorial(claseId, pagina, tamaño, usuarioId);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/sala/{codigoSala}/historial")
    public ResponseEntity<HistorialChatResponse> obtenerHistorialPorSala(
            @PathVariable String codigoSala,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamaño,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        HistorialChatResponse historial = chatService.obtenerHistorialPorCodigoSala(codigoSala, pagina, tamaño, usuarioId);
        return ResponseEntity.ok(historial);
    }

    @PutMapping("/mensaje/{mensajeId}")
    public ResponseEntity<MensajeChatDTO> editarMensaje(
            @PathVariable Long mensajeId,
            @RequestBody Map<String, String> request,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        String nuevoContenido = request.get("contenido");
        
        if (nuevoContenido == null || nuevoContenido.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            MensajeChatDTO mensaje = chatService.editarMensaje(mensajeId, nuevoContenido, usuarioId);
            return ResponseEntity.ok(mensaje);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/mensaje/{mensajeId}")
    public ResponseEntity<Void> eliminarMensaje(
            @PathVariable Long mensajeId,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        
        try {
            chatService.eliminarMensaje(mensajeId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clase/{claseId}/limpiar")
    public ResponseEntity<Void> limpiarChat(
            @PathVariable Long claseId,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        
        try {
            chatService.limpiarChatClase(claseId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/clase/{claseId}/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(
            @PathVariable Long claseId) {
        
        Long totalMensajes = chatService.contarMensajes(claseId);
        Map<String, Object> estadisticas = Map.of(
            "totalMensajes", totalMensajes,
            "timestamp", LocalDateTime.now()
        );
        return ResponseEntity.ok(estadisticas);
    }

    @PostMapping("/clase/{claseId}/sistema")
    public ResponseEntity<MensajeChatDTO> enviarMensajeSistema(
            @PathVariable Long claseId,
            @RequestBody Map<String, String> request,
            Principal principal) {
        
        // Solo instructores pueden enviar mensajes del sistema
        Long usuarioId = obtenerIdDesdeJWT(principal);
        String contenido = request.get("contenido");
        
        if (contenido == null || contenido.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            MensajeChatDTO mensaje = chatService.enviarMensajeSistema(claseId, contenido);
            return ResponseEntity.ok(mensaje);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/clase/{claseId}/permisos")
    public ResponseEntity<Map<String, Boolean>> verificarPermisos(
            @PathVariable Long claseId,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        boolean puedeParticipar = chatService.puedeParticiparEnChat(claseId, usuarioId);
        
        Map<String, Boolean> permisos = Map.of(
            "puedeParticipar", puedeParticipar,
            "puedeEnviarMensajes", puedeParticipar
        );
        
        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/clase/{claseId}/mensajes-recientes")
    public ResponseEntity<List<MensajeChatDTO>> obtenerMensajesRecientes(
            @PathVariable Long claseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            Principal principal) {
        
        Long usuarioId = obtenerIdDesdeJWT(principal);
        
        // Si no se especifica fecha, usar últimos 30 minutos
        if (desde == null) {
            desde = LocalDateTime.now().minusMinutes(30);
        }
        
        List<MensajeChatDTO> mensajes = chatService.obtenerMensajesRecientes(claseId, desde, usuarioId);
        return ResponseEntity.ok(mensajes);
    }

    // ✅ Manejo global de excepciones
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException e) {
        Map<String, String> error = Map.of(
            "error", "Recurso no encontrado",
            "mensaje", e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        Map<String, String> error = Map.of(
            "error", "Operación no permitida",
            "mensaje", e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = Map.of(
            "error", "Datos inválidos",
            "mensaje", e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private Long obtenerIdDesdeJWT(Principal principal) {
String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getCveUsuario();
    }
}

