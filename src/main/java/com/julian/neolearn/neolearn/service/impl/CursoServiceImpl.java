package com.julian.neolearn.neolearn.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        if (dto == null) {
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

    @Transactional
    @Override
    public CursoDTO guardarPortada(Long cursoId, MultipartFile file) throws IOException {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        String nombreOriginal = file.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));

        // Asegúrate de limpiar caracteres problemáticos si es necesario
        String nombreArchivo = "curso_" + cursoId + "_" + curso.getTitulo().replaceAll("[^a-zA-Z0-9]", "_") + extension;

        Path ruta = Paths.get("uploads/cursos").resolve(nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

        curso.setPortada(nombreArchivo);
        cursoRepository.save(curso);

        return cursoMapper.toDTO(curso);
    }

    @Transactional(readOnly = true)
    @Override
    public Resource obtenerPortada(String nombreArchivo) throws MalformedURLException {
        Path ruta = Paths.get("uploads/cursos").resolve(nombreArchivo).normalize();
        return new UrlResource(ruta.toUri());
    }

}
