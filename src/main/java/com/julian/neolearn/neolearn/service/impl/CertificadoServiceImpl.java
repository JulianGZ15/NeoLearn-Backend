package com.julian.neolearn.neolearn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.CertificadoDTO;
import com.julian.neolearn.neolearn.entity.Certificado;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Inscripcion;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.CertificadoMapper;
import com.julian.neolearn.neolearn.repository.CertificadoRepository;
import com.julian.neolearn.neolearn.repository.InscripcionRepository;
import com.julian.neolearn.neolearn.service.CertificadoService;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificadoServiceImpl implements CertificadoService {

    private final CertificadoRepository certificadoRepository;
    private final CertificadoMapper certificadoMapper;
    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoDTO> buscarCertificadoPorId(Long cveCertificado) {
        return certificadoRepository.findById(cveCertificado).map(certificadoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> listarCertificados() {
        return certificadoRepository.findAll().stream().map(certificadoMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public CertificadoDTO guardarCertificado(CertificadoDTO dto) {
        Certificado Certificado = certificadoMapper.toEntity(dto);

        if (dto.getCve_certificado() != null) {
            // Validar existencia previa
            Optional<Certificado> existente = certificadoRepository.findById(dto.getCve_certificado());
            if (existente.isEmpty()) {
                throw new EntityNotFoundException("Certificado no encontrada con ID: " + dto.getCve_certificado());
            }
        }

        Certificado = certificadoRepository.save(Certificado);
        return certificadoMapper.toDTO(Certificado);
    }

    @Override
    @Transactional
    public void borrarCertificadoPorId(Long cveCertificado) {

        certificadoRepository.deleteById(cveCertificado);
    }

    @Override
    @Transactional
    public CertificadoDTO generarCertificado(Long cve_inscripcion) throws Exception {

        Inscripcion inscripcion = inscripcionRepository.findById(cve_inscripcion)
                .orElseThrow(() -> new RuntimeException("Incipcion no encontrada"));

        Usuario usuario = inscripcion.getUsuario(); 

        Curso curso = inscripcion.getCurso();

        String nombreArchivo = "certificado_" + usuario.getCveUsuario() + "_" + curso.getCveCurso() + ".pdf";
        Path ruta = Paths.get("uploads/certificados").resolve(nombreArchivo);
        Files.createDirectories(ruta.getParent());

        // Generar PDF con OpenPDF
        String plantilla = "uploads/plantillas/plantilla_certificado.pdf"; // tu diseño base
        String destino = "uploads/certificados/"+ nombreArchivo;

        PdfReader reader = new PdfReader(plantilla);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destino));
        PdfContentByte cb = stamper.getOverContent(1);

        BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.setFontAndSize(font, 26);

        // Nombre del estudiante
        cb.beginText();
        cb.setTextMatrix(330, 265); // coordenadas X, Y
        cb.showText(usuario.getNombre());
        cb.endText();


        cb.setFontAndSize(font, 16);
        // Nombre del curso
        cb.beginText();
        cb.setTextMatrix(390, 340);
        cb.showText(curso.getTitulo());
        cb.endText();

        // Fecha
        cb.beginText();
        cb.setTextMatrix(680, 60);
        cb.showText(LocalDate.now().toString());
        cb.endText();

        // Insertar firma
        Image firma = Image.getInstance("uploads/empresa/firma.png");
        firma.setAbsolutePosition(240, 130); // posición
        firma.scaleAbsolute(75, 85); // tamaño
        cb.addImage(firma);

        // Insertar logo
        Image logo = Image.getInstance("uploads/empresa/logo.png");
        logo.setAbsolutePosition(40, 490);
        logo.scaleAbsolute(80, 80);
        cb.addImage(logo);

        stamper.close();
        reader.close();

        // Guardar entidad
        Certificado cert = new Certificado();
        cert.setInscripcion(inscripcion);
        cert.setFecha_emision(LocalDate.now());
        cert.setNombreArchivo(nombreArchivo);

        return certificadoMapper.toDTO(certificadoRepository.save(cert));
    }

}
