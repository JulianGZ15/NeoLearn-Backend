package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.CursoVendidoDTO;
import com.julian.neolearn.neolearn.dto.SuscripcionMensualDTO;
import com.julian.neolearn.neolearn.dto.UsuarioDTO;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.repository.EstadisticasEmpresaRepository;
import com.julian.neolearn.neolearn.service.EstadisticasEmpresaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticasEmpresaServiceImpl implements EstadisticasEmpresaService {

    private final EstadisticasEmpresaRepository repo;

    @Override
public Map<String, Object> obtenerResumenEstadisticas(Long cveEmpresa) {
    Map<String, Object> resumen = new LinkedHashMap<>();

    resumen.put("totalCursos", contarCursosPorEmpresa(cveEmpresa));
    resumen.put("gananciasMesActual", calcularGananciasMesActual(cveEmpresa));
    resumen.put("totalSuscripciones", contarTotalSuscripciones(cveEmpresa));
    resumen.put("totalEstudiantes", contarEstudiantesEmpresa(cveEmpresa));
    resumen.put("ultimosSuscriptores", encontrarUltimosCincoSuscriptores(cveEmpresa));
    resumen.put("cursosMasVendidos", encontrarCursosMasVendidos(cveEmpresa));
    resumen.put("suscripcionesPorMes", obtenerSuscripcionesPorMes(cveEmpresa));

    return resumen;
}


    @Override
    public Long contarCursosPorEmpresa(Long cveEmpresa) {
        return repo.contarCursosPorEmpresa(cveEmpresa);
    }

    @Override
    public Double calcularGananciasMesActual(Long cveEmpresa) {
        return repo.calcularGananciasMesActual(cveEmpresa);
    }

    @Override
    public Long contarTotalSuscripciones(Long cveEmpresa) {
        return repo.contarTotalSuscripciones(cveEmpresa);
    }

    @Override
    public Long contarEstudiantesEmpresa(Long cveEmpresa) {
        return repo.contarEstudiantesEmpresa(cveEmpresa);
    }

    @Override
public List<UsuarioDTO> encontrarUltimosCincoSuscriptores(Long cveEmpresa) {
    List<Object[]> resultados = repo.encontrarUltimosCincoSuscriptores(cveEmpresa);

    return resultados.stream().map(obj -> {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setCveUsuario(((Number)obj[0]).longValue());
        dto.setNombre((String)obj[1]);
        dto.setCorreo((String)obj[2]);
        return dto;
    }).toList();
}


    @Override
    public List<CursoVendidoDTO> encontrarCursosMasVendidos(Long cveEmpresa) {
        List<Object[]> resultados = repo.encontrarCursosMasVendidos(cveEmpresa);
        List<CursoVendidoDTO> cursos = new ArrayList<>();
        for (Object[] fila : resultados) {
            Long cveCurso = ((Number) fila[0]).longValue();
            String titulo = (String) fila[1];
            Long totalInscripciones = ((Number) fila[2]).longValue();
            Double totalGanado = ((Number) fila[3]).doubleValue();
            cursos.add(new CursoVendidoDTO(cveCurso, titulo, totalInscripciones, totalGanado));
        }
        return cursos;
    }

    @Override
    public List<SuscripcionMensualDTO> obtenerSuscripcionesPorMes(Long cveEmpresa) {
        List<Object[]> resultados = repo.obtenerSuscripcionesPorMes(cveEmpresa);
        List<SuscripcionMensualDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            Integer anio = ((Number) fila[0]).intValue();
            Integer mes = ((Number) fila[1]).intValue();
            Long total = ((Number) fila[2]).longValue();
            lista.add(new SuscripcionMensualDTO(anio, mes, total));
        }
        return lista;
    }
}
