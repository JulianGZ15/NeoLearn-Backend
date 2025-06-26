package com.julian.neolearn.neolearn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.julian.neolearn.neolearn.dto.SalaEnVivoDTO;
import com.julian.neolearn.neolearn.entity.SalaEnVivo;

@Mapper(componentModel = "spring", uses = {ClaseEnVivoMapper.class})
public interface SalaEnVivoMapper {

    @Mapping(target = "cursoId", source = "curso.cveCurso")
    @Mapping(target = "cursoNombre", source = "curso.titulo")
    @Mapping(target = "totalParticipantes", expression = "java(getTotalParticipantes(entity.getParticipantes()))")
    @Mapping(target = "clases", source = "clases")
    SalaEnVivoDTO toDTO(SalaEnVivo entity);

    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "clases", ignore = true)
    @Mapping(target = "participantes", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "activa", constant = "false")
    SalaEnVivo toEntity(SalaEnVivoDTO dto);

    
    // Mapper sin clases para evitar referencias circulares
    @Mapping(target = "cursoId", source = "curso.cveCurso")
    @Mapping(target = "cursoNombre", source = "curso.titulo")
    @Mapping(target = "totalParticipantes", expression = "java(getTotalParticipantes(entity.getParticipantes()))")
    @Mapping(target = "clases", ignore = true)
    SalaEnVivoDTO toDTOSinClases(SalaEnVivo entity);
    
    @Named("getTotalParticipantes")
    default Integer getTotalParticipantes(List<Long> participantes) {
        return participantes != null ? participantes.size() : 0;
    }
    

}
