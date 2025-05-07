package com.julian.neolearn.neolearn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.Usuario;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    // Obtener empresa a la que pertenece un usuario
    Optional<Empresa> findByUsuarios_cveUsuario(Long usuarioId);

    // Alternativa por correo (si ya lo tienes autenticado)
    Optional<Empresa> findByUsuarios_Correo(String correo);

    List<Empresa> findAllByUsuarios_cveUsuario(Long cve_usuario);

    Optional<Empresa> findByUsuariosContaining(Usuario usuario);

}

