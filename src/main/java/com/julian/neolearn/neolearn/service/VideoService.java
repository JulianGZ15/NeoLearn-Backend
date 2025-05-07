package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.VideoDTO;
public interface VideoService {

    Optional<VideoDTO> buscarVideoPorId(Long cveVideo);
    List<VideoDTO> listarVideos(Long cveCurso);
    VideoDTO guardarVideo(VideoDTO video, Long cveCurso);
    void borrarVideoPorId(Long cveVideo);
}
