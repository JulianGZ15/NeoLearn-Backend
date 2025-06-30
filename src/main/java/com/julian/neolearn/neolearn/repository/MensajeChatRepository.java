package com.julian.neolearn.neolearn.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.MensajeChat;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {

    // ✅ Método automático por ID de clase
    Page<MensajeChat> findByClaseEnVivo_CveClaseEnVivoOrderByTimestampDesc(Long claseId, Pageable pageable);

    // ✅ Método automático por código de sala (path anidado)
    Page<MensajeChat> findByClaseEnVivo_SalaEnVivo_CodigoSalaOrderByTimestampDesc(String codigoSala, Pageable pageable);

    // ✅ Contar mensajes
    Long countByClaseEnVivo_CveClaseEnVivo(Long claseId);

    // ✅ Mensajes recientes
    List<MensajeChat> findByClaseEnVivo_CveClaseEnVivoAndTimestampGreaterThanEqualOrderByTimestampAsc(Long claseId, LocalDateTime desde);

    // ✅ Eliminar por clase
    void deleteByClaseEnVivo_CveClaseEnVivo(Long claseId);
}

