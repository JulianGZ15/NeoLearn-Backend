package com.julian.neolearn.neolearn.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.dto.ProgramarClaseRequest;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo;
import com.julian.neolearn.neolearn.entity.Usuario;

@Mapper(componentModel = "spring", uses = {SalaEnVivoMapper.class, UsuarioMapper.class})
public interface ClaseEnVivoMapper {

    @Mapping(target = "salaId", source = "salaEnVivo.id")
    @Mapping(target = "codigoSala", source = "salaEnVivo.codigoSala")
    @Mapping(target = "instructorId", source = "instructor.cveUsuario")
    @Mapping(target = "instructorNombre", expression = "java(getInstructorNombre(entity.getInstructor()))")
    @Mapping(target = "cursoId", source = "salaEnVivo.curso.cveCurso")
    @Mapping(target = "cursoNombre", source = "salaEnVivo.curso.titulo")
    ClaseEnVivoDTO toDTO(ClaseEnVivo entity);

    @Mapping(target = "salaEnVivo", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "estado", constant = "PROGRAMADA")
    @Mapping(target = "finalizada", constant = "false")
    ClaseEnVivo toEntity(ClaseEnVivoDTO dto);

    List<ClaseEnVivoDTO> toDTOList(List<ClaseEnVivo> entities);

    @Named("getInstructorNombre")
    default String getInstructorNombre(Usuario instructor) {
        if (instructor == null) return null;
        return instructor.getNombre();
    }

    // Mapper para request de programación
    @Mapping(target = "cveClaseEnVivo", ignore = true)
    @Mapping(target = "fechaInicio", ignore = true)
    @Mapping(target = "fechaFin", ignore = true)
    @Mapping(target = "salaEnVivo", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "estado", constant = "PROGRAMADA")
    @Mapping(target = "finalizada", constant = "false")
    ClaseEnVivo fromRequest(ProgramarClaseRequest request);
}
