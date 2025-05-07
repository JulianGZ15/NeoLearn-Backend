package com.julian.neolearn.neolearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByCurso_CveCurso(Long cveCurso);
}
