package com.julian.neolearn.neolearn.service;

import java.util.List;

import com.julian.neolearn.neolearn.dto.TokenInvitacionDTO;


public interface InvitacionService {

    String generarTokenInvitacion();

    List<TokenInvitacionDTO> obtenerTokensPorEmpresa();

}
