package qr.uz.qrcodeconvertor;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/qr")
@AllArgsConstructor
public class Controller {

    private final QrCodeRepository qrCodeRepository;

    @PostMapping("/save")
    @SneakyThrows
    public HttpEntity<?> saveCertificate(@RequestBody CertificateDto certificateDto,
                                         @RequestParam MultipartFile file) {
        if (file.isEmpty() || file.getBytes().length == 0)
            return ResponseEntity.badRequest().body("image file is empty or no chosen");
        return saveNewCertificate(certificateDto, file);
    }

    @SneakyThrows
    public HttpEntity<?> saveNewCertificate(CertificateDto certificateDto, MultipartFile file) {
        QrCodeCertificate certificate = new QrCodeCertificate();
        certificate.setName(certificateDto.getName());
        certificate.setFullName(certificateDto.getFullName());
        certificate.setFile(file.getBytes());
        qrCodeRepository.save(certificate);
        return ResponseEntity.status(200).body("Certificate Id: " + certificate.getId());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") Long id) {
        Optional<QrCodeCertificate> certificate = qrCodeRepository.findById(id);
        if (certificate.isPresent() && certificate.get().getFile() != null) {
            byte[] file = certificate.get().getFile();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(file);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadCertificatePdf(@PathVariable Long id) {
        Optional<QrCodeCertificate> certificateOptional = qrCodeRepository.findById(id);
        if (certificateOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        QrCodeCertificate certificate = certificateOptional.get();
        byte[] pdfBytes;
        try {
            pdfBytes = PdfGenerator.generateCertificatePdf(certificate);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + certificate.getName() + ".pdf\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);

    }

    @GetMapping("/list")
    public ResponseEntity<List<byte[]>> getAllCertificates() {
        List<QrCodeCertificate> certificates = qrCodeRepository.findAll();
        if (certificates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<byte[]> certificateBytes = new ArrayList<>();

        for (QrCodeCertificate certificate : certificates) {
                byte[] file = certificate.getFile();
                certificateBytes.add(file);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(certificateBytes);
    }

}
