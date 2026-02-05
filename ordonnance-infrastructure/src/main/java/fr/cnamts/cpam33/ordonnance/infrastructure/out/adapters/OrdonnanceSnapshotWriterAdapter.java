package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.pdf.BaseFont;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceSnapshotWriter;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.OrdonnancePdfViewMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.views.OrdonnancePdfView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Component
public class OrdonnanceSnapshotWriterAdapter implements Adapter, OrdonnanceSnapshotWriter {

    private static final Logger logger = LoggerFactory.getLogger(OrdonnanceSnapshotWriterAdapter.class);

    private final SpringTemplateEngine templateEngine;

    public OrdonnanceSnapshotWriterAdapter(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void writeSnapshot(OrdonnanceSnapshot snapshot) {
        try {
            OrdonnancePdfView view = OrdonnancePdfViewMapper.from(snapshot);
            Context context = new Context();
            context.setVariable("ordonnance", view);
            context.setVariable("dateEmission", LocalDate.now().toString());
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_SNAPSHOT_GENERATION_ERROR);
        }
    }

}
