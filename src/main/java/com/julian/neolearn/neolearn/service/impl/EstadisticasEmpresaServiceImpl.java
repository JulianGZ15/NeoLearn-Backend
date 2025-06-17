package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.CursoVendidoDTO;
import com.julian.neolearn.neolearn.dto.SuscripcionMensualDTO;
import com.julian.neolearn.neolearn.dto.UsuarioDTO;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.repository.EmpresaRepository;
import com.julian.neolearn.neolearn.repository.EstadisticasEmpresaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.EstadisticasEmpresaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticasEmpresaServiceImpl implements EstadisticasEmpresaService {

    private final EstadisticasEmpresaRepository repo;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    @Override
public Map<String, Object> obtenerResumenEstadisticas( ) {
    Map<String, Object> resumen = new LinkedHashMap<>();

    resumen.put("totalCursos", contarCursosPorEmpresa());
    resumen.put("gananciasMesActual", calcularGananciasMesActual());
    resumen.put("totalSuscripciones", contarTotalSuscripciones());
    resumen.put("totalEstudiantes", contarEstudiantesEmpresa());
    resumen.put("ultimosSuscriptores", encontrarUltimosCincoSuscriptores());
    resumen.put("cursosMasVendidos", encontrarCursosMasVendidos());
    resumen.put("suscripcionesPorMes", obtenerSuscripcionesPorMes());

    return resumen;
}


    @Override
    public Long contarCursosPorEmpresa( ) {
                String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();
        return repo.contarCursosPorEmpresa(cveEmpresa);
    }

    @Override
    public Double calcularGananciasMesActual() {
                        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();
        return repo.calcularGananciasMesActual(cveEmpresa);
    }

    @Override
    public Long contarTotalSuscripciones( ) {
                        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();
        return repo.contarTotalSuscripciones(cveEmpresa);
    }

    @Override
    public Long contarEstudiantesEmpresa( ) {
                        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();
        return repo.contarEstudiantesEmpresa(cveEmpresa);
    }

    @Override
public List<UsuarioDTO> encontrarUltimosCincoSuscriptores( ) {
                    String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();
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
    public List<CursoVendidoDTO> encontrarCursosMasVendidos( ) {
                        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();

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
    public List<SuscripcionMensualDTO> obtenerSuscripcionesPorMes( ) {
                        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Long cveEmpresa = empresa.getCveEmpresa();

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
