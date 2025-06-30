package com.julian.neolearn.neolearn.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.ConfiguracionDTO;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.mapper.ConfiguracionMapper;
import com.julian.neolearn.neolearn.repository.ConfiguracionRepository;
import com.julian.neolearn.neolearn.repository.EmpresaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.ConfiguracionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final ConfiguracionMapper configuracionMapper;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    public ConfiguracionDTO obtenerConfiguracionPorEmpresa(Long cveEmpresa) {
        var configuracion = configuracionRepository.findByEmpresa_CveEmpresa(cveEmpresa);
        if (configuracion == null) {
            throw new EntityNotFoundException("Configuración no encontrada para la empresa con cve: " + cveEmpresa);
        }
        return configuracionMapper.toDTO(configuracion);
    }

    @Transactional
    @Override
    public ConfiguracionDTO guardarConfiguracion(ConfiguracionDTO configuracionDTO) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden configurar Certificados");
        }

        Empresa empresa = empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        var configuracionEntity = configuracionMapper.toEntity(configuracionDTO);
        configuracionEntity.setEmpresa(empresa);
        
        configuracionEntity = configuracionRepository.save(configuracionEntity);
        return configuracionMapper.toDTO(configuracionEntity);
    }

    @Transactional
    @Override
    public ConfiguracionDTO guardarFirma(MultipartFile file) throws IOException {
        validarUsuarioEmpresarial();
        validarArchivo(file);
        
        Empresa empresa = obtenerEmpresaUsuarioActual();
        Long cveEmpresa = empresa.getCveEmpresa();
        
        // ✅ Obtener o crear configuración
        ConfiguracionDTO configuracionDTO = obtenerOCrearConfiguracion(cveEmpresa);

        String nombreArchivo = generarNombreArchivo(file, "firmaCertificado", empresa);
        Path ruta = construirRutaArchivo(nombreArchivo, empresa);
        
        guardarArchivo(file, ruta);

        configuracionDTO.setFirma(nombreArchivo);
        
        if (configuracionDTO.getCveEmpresa() == null) {
            configuracionDTO.setCveEmpresa(cveEmpresa);
        }
        
        return guardarConfiguracion(configuracionDTO);
    }

    @Transactional
    @Override
    public ConfiguracionDTO guardarLogo(MultipartFile file) throws IOException {
        validarUsuarioEmpresarial();
        validarArchivo(file);
        
        Empresa empresa = obtenerEmpresaUsuarioActual();
        Long cveEmpresa = empresa.getCveEmpresa();
        
        // ✅ Obtener o crear configuración
        ConfiguracionDTO configuracionDTO = obtenerOCrearConfiguracion(cveEmpresa);

        String nombreArchivo = generarNombreArchivo(file, "logoCertificado", empresa);
        Path ruta = construirRutaArchivo(nombreArchivo, empresa);
        
        guardarArchivo(file, ruta);

        configuracionDTO.setLogo(nombreArchivo);
        
        if (configuracionDTO.getCveEmpresa() == null) {
            configuracionDTO.setCveEmpresa(cveEmpresa);
        }
        
        return guardarConfiguracion(configuracionDTO);
    }

    @Transactional
    @Override
    public ConfiguracionDTO guardarFirmante(String firmante) {
        validarUsuarioEmpresarial();
        
        Empresa empresa = obtenerEmpresaUsuarioActual();
        Long cveEmpresa = empresa.getCveEmpresa();
        
        // ✅ Obtener o crear configuración
        ConfiguracionDTO configuracionDTO = obtenerOCrearConfiguracion(cveEmpresa);

        configuracionDTO.setFirmante(firmante);
        
        if (configuracionDTO.getCveEmpresa() == null) {
            configuracionDTO.setCveEmpresa(cveEmpresa);
        }
        
        return guardarConfiguracion(configuracionDTO);
    }

    // ✅ NUEVO MÉTODO AUXILIAR
    /**
     * Obtiene la configuración existente o crea una nueva si no existe
     */
    private ConfiguracionDTO obtenerOCrearConfiguracion(Long cveEmpresa) {
        try {
            return obtenerConfiguracionPorEmpresa(cveEmpresa);
        } catch (EntityNotFoundException e) {
            // Si no existe, crear una nueva configuración
            ConfiguracionDTO nuevaConfiguracion = new ConfiguracionDTO();
            nuevaConfiguracion.setCveEmpresa(cveEmpresa);
            return nuevaConfiguracion;
        }
    }

    // MÉTODOS AUXILIARES EXISTENTES
    private void validarUsuarioEmpresarial() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("Solo usuarios empresariales pueden configurar certificados");
        }
    }

    private void validarArchivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo no puede estar vacío");
        }
        
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new RuntimeException("Solo se permiten archivos de imagen");
        }
        
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("El archivo no puede ser mayor a 5MB");
        }
    }

    private Empresa obtenerEmpresaUsuarioActual() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return empresaRepository.findByUsuarios_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));
    }

    private String generarNombreArchivo(MultipartFile file, String prefijo, Empresa empresa) {
        String nombreOriginal = file.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));
        return prefijo + "_" + empresa.getCveEmpresa() + "_" + 
               empresa.getNombre().replaceAll("[^a-zA-Z0-9]", "_") + extension;
    }

    private Path construirRutaArchivo(String nombreArchivo, Empresa empresa) {
        return Paths.get("uploads/certificados/configuracion/" + empresa.getNombre())
                   .resolve(nombreArchivo);
    }

    private void guardarArchivo(MultipartFile file, Path ruta) throws IOException {
        Files.createDirectories(ruta.getParent());
        Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
    }
}
