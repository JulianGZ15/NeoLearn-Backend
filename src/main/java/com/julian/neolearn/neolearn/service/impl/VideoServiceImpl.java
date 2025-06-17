package com.julian.neolearn.neolearn.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.VideoDTO;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Video;
import com.julian.neolearn.neolearn.mapper.VideoMapper;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.repository.VideoRepository;
import com.julian.neolearn.neolearn.service.VideoService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final CursoRepository cursoRepository;



    @Transactional(readOnly = true)
    @Override
    public Optional<VideoDTO> buscarVideoPorId(Long cveVideo) {
        return videoRepository.findById(cveVideo)
                .map(videoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VideoDTO> listarVideos(Long cveCurso) {

        List<Video> videos = videoRepository.findByCurso_CveCurso(cveCurso);

        return videos.stream()
                .map(videoMapper::toDTO)
                .toList();
    }

    @Transactional
    public VideoDTO guardarVideo(VideoDTO dto, Long cveCurso) {
        if (dto == null || cveCurso == null) {
            throw new IllegalArgumentException("El video y el ID del curso no pueden ser nulos");
        }

        Video video = videoMapper.toEntity(dto);
        Curso curso = cursoRepository.findById(cveCurso)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + cveCurso));
                
        video.setCurso(curso);
        video = videoRepository.save(video);
        return videoMapper.toDTO(video);
    }

    @Transactional
    @Override
    public void borrarVideoPorId(Long cveVideo) {
        Video video = videoRepository.findById(cveVideo)
                .orElseThrow(() -> new EntityNotFoundException("Video no encontrado con ID: " + cveVideo));

        videoRepository.delete(video);
    }

     @Transactional
    @Override
    public VideoDTO guardarPortada(Long videoId, MultipartFile file) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        String nombreOriginal = file.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));

        // Asegúrate de limpiar caracteres problemáticos si es necesario
        String nombreArchivo = "Video_" + videoId + "_" + video.getTitulo().replaceAll("[^a-zA-Z0-9]", "_") + extension;

        Path ruta = Paths.get("uploads/video").resolve(nombreArchivo);
        Files.createDirectories(ruta.getParent());
        Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

        video.setPortada(nombreArchivo);
        videoRepository.save(video);

        return videoMapper.toDTO(video);
    }

    @Transactional(readOnly = true)
    @Override
    public Resource obtenerPortada(String nombreArchivo) throws MalformedURLException {
        Path ruta = Paths.get("uploads/video").resolve(nombreArchivo).normalize();
        return new UrlResource(ruta.toUri());
    }

}
