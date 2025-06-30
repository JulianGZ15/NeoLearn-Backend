package com.julian.neolearn.neolearn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.entity.MensajeChat;
import com.julian.neolearn.neolearn.entity.Usuario;

@Mapper(componentModel = "spring", uses = {ClaseEnVivoMapper.class, UsuarioMapper.class})
public interface MensajeChatMapper {

    @Mapping(target = "claseEnVivoId", source = "claseEnVivo.cveClaseEnVivo")
    @Mapping(target = "codigoSala", source = "claseEnVivo.salaEnVivo.codigoSala")
    @Mapping(target = "usuarioId", source = "usuario.cveUsuario")
    @Mapping(target = "usuarioNombre", expression = "java(getUsuarioNombre(entity.getUsuario()))")
    @Mapping(target = "usuarioAvatar", source = "usuario.fotoperfil")
    @Mapping(target = "esMio", ignore = true) // Se establece en el servicio
    MensajeChatDTO toDTO(MensajeChat entity);

    @Mapping(target = "claseEnVivo", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "editado", constant = "false")
    @Mapping(target = "fechaEdicion", ignore = true)
    MensajeChat toEntity(MensajeChatDTO dto);

    List<MensajeChatDTO> toDTOList(List<MensajeChat> entities);

    @Named("getUsuarioNombre")
    default String getUsuarioNombre(Usuario usuario) {
        if (usuario == null) return "Usuario Anónimo";
        return usuario.getNombre();
    }

    // Mapper específico para establecer si es mensaje propio
    @Mapping(target = "claseEnVivoId", source = "mensaje.claseEnVivo.cveClaseEnVivo")
    @Mapping(target = "codigoSala", source = "mensaje.claseEnVivo.salaEnVivo.codigoSala")
    @Mapping(target = "usuarioId", source = "mensaje.usuario.cveUsuario")
    @Mapping(target = "usuarioNombre", expression = "java(getUsuarioNombre(mensaje.getUsuario()))")
    @Mapping(target = "usuarioAvatar", source = "mensaje.usuario.fotoperfil")
    @Mapping(target = "esMio", expression = "java(mensaje.getUsuario().getCveUsuario().equals(usuarioActualId))")
    MensajeChatDTO toDTOWithUser(MensajeChat mensaje, Long usuarioActualId);
}
