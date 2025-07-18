package com.julian.neolearn.neolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Comision;

@Repository
public interface ComisionRepository extends JpaRepository<Comision, Long> {
}
