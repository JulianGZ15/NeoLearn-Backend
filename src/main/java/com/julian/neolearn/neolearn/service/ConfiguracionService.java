package com.julian.neolearn.neolearn.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.ConfiguracionDTO;

public interface ConfiguracionService {

 ConfiguracionDTO obtenerConfiguracionPorEmpresa(Long cveEmpresa);
    ConfiguracionDTO guardarConfiguracion(ConfiguracionDTO configuracionDTO);
    ConfiguracionDTO guardarFirma(MultipartFile file) throws IOException;
    ConfiguracionDTO guardarLogo(MultipartFile file) throws IOException;
    ConfiguracionDTO guardarFirmante(String firmante);
}
