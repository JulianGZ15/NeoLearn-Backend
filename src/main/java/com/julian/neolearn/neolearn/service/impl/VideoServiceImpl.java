package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
