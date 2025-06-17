package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.TokenInvitacionDTO;
import com.julian.neolearn.neolearn.entity.TokenInvitacionEmpresa;


@Mapper(componentModel = "spring")
public interface TokenInvitacionMapper {

    TokenInvitacionDTO toDTO (TokenInvitacionEmpresa tokenInvitacionEmpresa);
    TokenInvitacionEmpresa toEntity (TokenInvitacionDTO tokenInvitacionDTO);



}
