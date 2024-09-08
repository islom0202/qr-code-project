package qr.uz.qrcodeconvertor;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfGenerator {
    public static byte[] generateCertificatePdf(QrCodeCertificate certificate) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            if (certificate.getFile() != null) {
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, certificate.getFile(), certificate.getName());
                contentStream.drawImage(pdImage, 100, 500, 400, 400); // Adjust the size and position as needed
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}
