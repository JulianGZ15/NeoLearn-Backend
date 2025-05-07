package com.julian.neolearn.neolearn.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.VideoDTO;
import com.julian.neolearn.neolearn.service.VideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/{cveCurso}/videos")
    public ResponseEntity<List<VideoDTO>> listarVideos(@PathVariable Long cveCurso) {
        List<VideoDTO> videos = videoService.listarVideos(cveCurso);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{cveVideo}")
    public ResponseEntity<VideoDTO> obtenerVideoPorId(@PathVariable Long cveVideo) {
        return videoService.buscarVideoPorId(cveVideo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cveCurso}")
    public ResponseEntity<VideoDTO> crearVideo(@RequestBody VideoDTO dto, @PathVariable Long cveCurso) {
        VideoDTO nuevoVideo = videoService.guardarVideo(dto, cveCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVideo);
    }

    @PutMapping("/{cveCurso}")
    public ResponseEntity<VideoDTO> actualizarVideo(@PathVariable Long cveCurso, @RequestBody VideoDTO dto) {
        VideoDTO videoActualizada = videoService.guardarVideo(dto, cveCurso); // Aquí se puede pasar el cveCurso si es necesario
        return ResponseEntity.ok(videoActualizada);
     }

    @DeleteMapping("/{cveVideo}")
    public ResponseEntity<Void> eliminarVideo(@PathVariable Long cveVideo) {
        videoService.borrarVideoPorId(cveVideo);
        return ResponseEntity.noContent().build();
     }
}
