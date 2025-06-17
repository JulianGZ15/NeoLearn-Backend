package com.julian.neolearn.neolearn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.TokenInvitacionEmpresa;

@Repository
public interface TokenInvitacionEmpresaRepository extends JpaRepository<TokenInvitacionEmpresa, Long> {
    Optional<TokenInvitacionEmpresa> findByToken(String token);
    List<TokenInvitacionEmpresa> findByEmpresaCveEmpresa(Long cveEmpresa);
}

