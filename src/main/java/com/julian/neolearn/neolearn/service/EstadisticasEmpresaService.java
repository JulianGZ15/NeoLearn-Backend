package com.julian.neolearn.neolearn.service;

import java.util.List;
import java.util.Map;

import com.julian.neolearn.neolearn.dto.CursoVendidoDTO;
import com.julian.neolearn.neolearn.dto.SuscripcionMensualDTO;
import com.julian.neolearn.neolearn.dto.UsuarioDTO;

public interface EstadisticasEmpresaService {
    Map<String, Object> obtenerResumenEstadisticas();
    Long contarCursosPorEmpresa();
    Double calcularGananciasMesActual();
    Long contarTotalSuscripciones();
    Long contarEstudiantesEmpresa( );
    List<UsuarioDTO> encontrarUltimosCincoSuscriptores( );
    List<CursoVendidoDTO> encontrarCursosMasVendidos( );
    List<SuscripcionMensualDTO> obtenerSuscripcionesPorMes( );
}
