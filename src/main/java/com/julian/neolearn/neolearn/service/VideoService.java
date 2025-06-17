package com.julian.neolearn.neolearn.service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.VideoDTO;
public interface VideoService {

    Optional<VideoDTO> buscarVideoPorId(Long cveVideo);
    List<VideoDTO> listarVideos(Long cveCurso);
    VideoDTO guardarVideo(VideoDTO video, Long cveCurso);
    void borrarVideoPorId(Long cveVideo);
    VideoDTO guardarPortada(Long cursoId, MultipartFile file) throws IOException;
    Resource obtenerPortada(String nombreArchivo) throws MalformedURLException;
}
