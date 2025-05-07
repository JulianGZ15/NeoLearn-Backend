package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.CursoDTO;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.mapper.CursoMapper;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.repository.EmpresaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.CursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoMapper cursoMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<CursoDTO> buscarCursoPorId(Long cveCurso) {
        return cursoRepository.findById(cveCurso)
                .map(cursoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CursoDTO> listarCursos() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        List<Curso> cursos = cursoRepository.findByEmpresa(empresa);

        return cursos.stream()
                .map(cursoMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public CursoDTO guardarCurso(CursoDTO dto) {
        if (dto == null ) {
            throw new IllegalArgumentException("El video y el ID del curso no pueden ser nulos");
        }
        
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Opcional: validar rol
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden crear cursos");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        Curso curso = cursoMapper.toEntity(dto);
        curso.setEmpresa(empresa);
        curso.setFecha_publicacion(LocalDate.now());

        Curso saved = cursoRepository.save(curso);
        return cursoMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public void borrarCursoPorId(Long cveCurso) {
        Curso curso = cursoRepository.findById(cveCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        cursoRepository.delete(curso);
    }
}
