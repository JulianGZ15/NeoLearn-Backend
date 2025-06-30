package com.julian.neolearn.neolearn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.CertificadoDTO;
import com.julian.neolearn.neolearn.dto.ConfiguracionDTO;
import com.julian.neolearn.neolearn.entity.Certificado;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.Inscripcion;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.CertificadoMapper;
import com.julian.neolearn.neolearn.repository.CertificadoRepository;
import com.julian.neolearn.neolearn.repository.InscripcionRepository;
import com.julian.neolearn.neolearn.service.CertificadoService;
import com.julian.neolearn.neolearn.service.ConfiguracionService;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

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
    private final ConfiguracionService configuracionService;

    @Override
    @Transactional
    public CertificadoDTO guardarCertificado(CertificadoDTO certificadoDTO) {
        Certificado certificado = certificadoMapper.toEntity(certificadoDTO);
        Certificado saved = certificadoRepository.save(certificado);
        return certificadoMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> listarCertificados() {
        List<Certificado> certificados = certificadoRepository.findAll();
        return certificados.stream()
                .map(certificadoMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void borrarCertificadoPorId(Long id) {
        certificadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoDTO> buscarCertificadoPorId(Long id) {
        return certificadoRepository.findById(id)
                .map(certificadoMapper::toDTO);
    }

    @Override
    @Transactional
    public CertificadoDTO generarCertificado(Long cve_inscripcion) throws Exception {
        Inscripcion inscripcion = inscripcionRepository.findById(cve_inscripcion)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        Usuario usuario = inscripcion.getUsuario(); 
        Curso curso = inscripcion.getCurso();
        
        Empresa empresa = obtenerEmpresaDelCurso(curso);
        Long cveEmpresa = empresa.getCveEmpresa(); // Necesitas implementar este método
        ConfiguracionDTO configuracion = configuracionService.obtenerConfiguracionPorEmpresa(cveEmpresa);

        String nombreArchivo = "certificado_" + usuario.getCveUsuario() + "_" + curso.getCveCurso() + ".pdf";
        Path ruta = Paths.get("uploads/certificados").resolve(nombreArchivo);
        Files.createDirectories(ruta.getParent());

        // Generar PDF con configuración dinámica
        generarPDFConConfiguracion(inscripcion, configuracion, nombreArchivo);

        // Guardar entidad
        Certificado cert = new Certificado();
        cert.setInscripcion(inscripcion);
        cert.setFecha_emision(LocalDate.now());
        cert.setNombreArchivo(nombreArchivo);

        return certificadoMapper.toDTO(certificadoRepository.save(cert));
    }

    private void generarPDFConConfiguracion(Inscripcion inscripcion, ConfiguracionDTO configuracion, String nombreArchivo) throws Exception {
        Usuario usuario = inscripcion.getUsuario();
        Curso curso = inscripcion.getCurso();
        
        String plantilla = "uploads/plantillas/plantilla_certificado.pdf";
        String destino = "uploads/certificados/" + nombreArchivo;

        PdfReader reader = new PdfReader(plantilla);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destino));
        PdfContentByte cb = stamper.getOverContent(1);

        BaseFont font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        
        // Datos del certificado
        agregarTextosCertificado(cb, font, usuario, curso, configuracion);
        
        // Imágenes dinámicas basadas en configuración
        agregarImagenesCertificado(cb, configuracion, curso);

        stamper.close();
        reader.close();
    }

    private void agregarTextosCertificado(PdfContentByte cb, BaseFont font, Usuario usuario, Curso curso, ConfiguracionDTO configuracion) throws Exception {
        // Nombre del estudiante
        cb.setFontAndSize(font, 26);
        cb.beginText();
        cb.setTextMatrix(330, 265);
        cb.showText(usuario.getNombre());
        cb.endText();

        // Nombre del curso
        cb.setFontAndSize(font, 16);
        cb.beginText();
        cb.setTextMatrix(390, 340);
        cb.showText(curso.getTitulo());
        cb.endText();

        // Fecha
        cb.beginText();
        cb.setTextMatrix(680, 60);
        cb.showText(LocalDate.now().toString());
        cb.endText();

        // **NUEVO**: Nombre del firmante (si existe)
        if (configuracion.getFirmante() != null && !configuracion.getFirmante().isEmpty()) {
            cb.setFontAndSize(font, 12);
            cb.beginText();
            cb.setTextMatrix(240, 100); // Ajusta la posición según tu plantilla
            cb.showText(configuracion.getFirmante());
            cb.endText();
        }
    }

    private void agregarImagenesCertificado(PdfContentByte cb, ConfiguracionDTO configuracion, Curso curso) throws Exception {
        Empresa empresa = obtenerEmpresaDelCurso(curso);
        String rutaConfiguracion = "uploads/certificados/configuracion/" + empresa.getNombre();

        // **MEJORADO**: Firma dinámica
        if (configuracion.getFirma() != null && !configuracion.getFirma().isEmpty()) {
            String rutaFirma = rutaConfiguracion + "/" + configuracion.getFirma();
            if (Files.exists(Paths.get(rutaFirma))) {
                Image firma = Image.getInstance(rutaFirma);
                firma.setAbsolutePosition(240, 130);
                firma.scaleAbsolute(75, 85);
                cb.addImage(firma);
            }
        }

        // **MEJORADO**: Logo dinámico
        if (configuracion.getLogo() != null && !configuracion.getLogo().isEmpty()) {
            String rutaLogo = rutaConfiguracion + "/" + configuracion.getLogo();
            if (Files.exists(Paths.get(rutaLogo))) {
                Image logo = Image.getInstance(rutaLogo);
                logo.setAbsolutePosition(40, 490);
                logo.scaleAbsolute(80, 80);
                cb.addImage(logo);
            }
        }
    }

    // Método auxiliar que necesitas implementar según tu modelo de datos
    private Empresa obtenerEmpresaDelCurso(Curso curso) {
        return curso.getEmpresa(); // Ejemplo
    }
}
