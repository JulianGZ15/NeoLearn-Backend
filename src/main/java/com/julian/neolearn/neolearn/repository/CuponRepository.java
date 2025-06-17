package com.julian.neolearn.neolearn.repository;

import java.lang.foreign.Linker.Option;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Cupon;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {
    List<Cupon> findByCurso_CveCurso(Long cveCurso);
}
