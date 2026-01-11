package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.services.TraceService;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.TraceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.TraceApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.DatabaseActeMetierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/traces")
public class TraceController {

    private static final Logger logger = LoggerFactory.getLogger(TraceController.class);

    private final TraceService traceService;
    private final TraceApiMapper traceApiMapper;
    private final DatabaseActeMetierResolver resolver;

    public TraceController(TraceService traceService, TraceApiMapper traceApiMapper, DatabaseActeMetierResolver resolver) {
        this.traceService = traceService;
        this.traceApiMapper = traceApiMapper;
        this.resolver = resolver;
    }

    @PostMapping
    public ResponseEntity<?> createTrace(@RequestBody TraceDto trace) {
        logger.info("trace : {}", trace.toString());
        List<TraceAttribute> traceAttributes = new ArrayList<>();
        traceService.trace(resolver.resolveByCode(trace.acteMetierCode()), traceApiMapper.mapMedecin(trace.medecinId()), new TraceContext(traceAttributes));
        return ResponseEntity.ok().build();
    }

}
