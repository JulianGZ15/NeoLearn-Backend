package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.EmpresaDTO;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.mapper.EmpresaMapper;
import com.julian.neolearn.neolearn.repository.EmpresaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.EmpresaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaMapper empresaMapper;

    @Override
    public Optional<EmpresaDTO> buscarEmpresaPorId(Long cveEmpresa) {
        return empresaRepository.findById(cveEmpresa)
                .map(empresaMapper::toDTO);
    }

    @Override
    public List<EmpresaDTO> listarEmpresas() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Empresa> empresas = empresaRepository.findByUsuarios_Correo(correo)
                .map(List::of) // ← Como retorna Optional<Empresa>, lo encapsulamos como lista
                .orElseThrow(() -> new RuntimeException("No se encontraron empresas para el usuario"));

        return empresas.stream()
                .map(empresaMapper::toDTO)
                .toList();
    }

    @Override
    public EmpresaDTO guardarEmpresa(EmpresaDTO dto) {
        Empresa empresa = empresaMapper.toEntity(dto);
    
        // Obtener usuario autenticado
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden registrar empresas");
        }
    
        empresa.getUsuarios().add(usuario); // Asociar el usuario autenticado
    
        // Si se especifican otros usuarios en el DTO
        if (dto.getCveUsuarios() != null) {
            List<Usuario> usuariosAdicionales = usuarioRepository.findAllById(dto.getCveUsuarios());
            empresa.getUsuarios().addAll(usuariosAdicionales);
        }
    
        empresa = empresaRepository.save(empresa);
        return empresaMapper.toDTO(empresa);
    }
    

    @Override
    public void borrarEmpresaPorId(Long cveEmpresa) {
        empresaRepository.deleteById(cveEmpresa);
    }

    @Override
    public List<EmpresaDTO> buscarEmpresaPorUsuario(Long cveUsuario) {
        Usuario usuario = usuarioRepository.findById(cveUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Empresa> empresas = empresaRepository.findAllByUsuarios_cveUsuario(usuario.getCveUsuario());

        if (empresas.isEmpty()) {
            throw new RuntimeException("El usuario no tiene empresas asociadas");
        }

        return empresas.stream()
                .map(empresaMapper::toDTO)
                .toList();
    }
}
