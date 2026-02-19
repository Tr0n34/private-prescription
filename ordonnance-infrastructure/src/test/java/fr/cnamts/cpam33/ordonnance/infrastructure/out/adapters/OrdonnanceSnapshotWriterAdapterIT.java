package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import com.lowagie.text.pdf.BaseFont;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.MedecinFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PrescriptionFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;
import fr.cnamts.cpam33.ordonnance.infrastructure.contexts.TestPdfConfig;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.OrdonnancePdfViewMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.views.OrdonnancePdfView;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        TestPdfConfig.class,
        OrdonnanceSnapshotWriterAdapter.class
})
@ActiveProfiles("integration")
class OrdonnanceSnapshotWriterAdapterIT {

    private final String DATE_FORMATTER_PATTERN = "ddMMyyyyHHmmss";

    @Autowired
    TestPdfConfig testPdfConfig;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Test
    void should_generate_valid_pdf_from_ordonnance_snapshot() throws Exception {
        OrdonnanceSnapshot snapshot = fakeSnapshot();
        OrdonnanceSnapshotWriterAdapter writer = new OrdonnanceSnapshotWriterAdapter(templateEngine);
        byte[] pdf = generatePdf(writer, snapshot);
        Path pdfFile = writePdf(pdf, "ordonnance-snapshot-valide");
        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(100);
        assertThat(new String(pdf, 0, 4)).isEqualTo("%PDF");
        try ( PDDocument document = Loader.loadPDF(pdf )) {
            assertThat(document.getNumberOfPages()).isGreaterThanOrEqualTo(1);
        }
    }

    private byte[] generatePdf(
            OrdonnanceSnapshotWriterAdapter writer,
            OrdonnanceSnapshot snapshot
    ) throws Exception {
        OrdonnancePdfView view = OrdonnancePdfViewMapper.from(snapshot);
        Context context = new Context();
        context.setVariable("ordonnance", view);
        context.setVariable("dateEmission", "2025-01-01");
        String html = templateEngine.process("ordonnance", context);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont(
                "/static/fonts/Roboto-Regular.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream.toByteArray();
    }

    private OrdonnanceSnapshot fakeSnapshot() {
        return new OrdonnanceSnapshot(Ordonnance.of(
                    new OrdonnanceId("123456"),
                    PatientFixtures.patientValide(),
                    MedecinFixtures.medecinValide(),
                    PrescriptionFixtures.twoPrescription()),
                new Signature("54649865ecse56f4qes5f54"),
                LocalDateTime.now());
    }

    /**
     * Test d'affichage à effectuer à part
     * @param pdf
     * @throws Exception
     */
    private Path writePdf(byte[] pdf, String name) throws Exception {
        Path dir = Paths.get(testPdfConfig.getOutputDir().toUri());
        Files.createDirectories(dir);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN));
        Path file = dir.resolve(name + "-" + timestamp + ".pdf");
        Files.write(file, pdf);
        System.out.println("PDF généré : " + file.toAbsolutePath());
        return file;
    }

}
