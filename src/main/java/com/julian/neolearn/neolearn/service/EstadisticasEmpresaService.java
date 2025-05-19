package com.julian.neolearn.neolearn.service;

import java.util.List;
import java.util.Map;

import com.julian.neolearn.neolearn.dto.CursoVendidoDTO;
import com.julian.neolearn.neolearn.dto.SuscripcionMensualDTO;
import com.julian.neolearn.neolearn.dto.UsuarioDTO;

public interface EstadisticasEmpresaService {
    Map<String, Object> obtenerResumenEstadisticas(Long cveEmpresa);
    Long contarCursosPorEmpresa(Long cveEmpresa);
    Double calcularGananciasMesActual(Long cveEmpresa);
    Long contarTotalSuscripciones(Long cveEmpresa);
    Long contarEstudiantesEmpresa(Long cveEmpresa);
    List<UsuarioDTO> encontrarUltimosCincoSuscriptores(Long cveEmpresa);
    List<CursoVendidoDTO> encontrarCursosMasVendidos(Long cveEmpresa);
    List<SuscripcionMensualDTO> obtenerSuscripcionesPorMes(Long cveEmpresa);
}
