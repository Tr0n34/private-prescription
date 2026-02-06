package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.admin;

import fr.cnamts.cpam33.ordonnance.application.services.TraceService;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces.TraceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces.WorkerStatusDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.TraceApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.DatabaseActeMetierResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.TraceWriterSupervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/traces")
public class TraceController {

    private static final Logger logger = LoggerFactory.getLogger(TraceController.class);

    private final TraceService traceService;
    private final TraceApiMapper traceApiMapper;
    private final DatabaseActeMetierResolver resolver;
    private final TraceWriterSupervisor traceWriterSupervisor;

    public TraceController(TraceService traceService,
                           TraceApiMapper traceApiMapper,
                           DatabaseActeMetierResolver resolver,
                           TraceWriterSupervisor traceWriterSupervisor) {
        this.traceService = traceService;
        this.traceApiMapper = traceApiMapper;
        this.resolver = resolver;
        this.traceWriterSupervisor = traceWriterSupervisor;
    }

    @PostMapping
    public ResponseEntity<?> createTrace(@RequestBody TraceDto trace, @RequestHeader("userId") String userId) {
        logger.info("trace : {}", trace.toString());
        List<TraceAttribute> traceAttributes = new ArrayList<>();
        traceService.trace(resolver.resolveByCode(trace.acteMetierCode()),
                traceApiMapper.mapUtilisateur(userId), new TraceContext(traceAttributes));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workers/status")
    public ResponseEntity<WorkerStatusDto> isRunning() {
        return ResponseEntity.ok(new WorkerStatusDto(traceWriterSupervisor.isRunning()));
    }

    @PatchMapping("/workers")
    public ResponseEntity<WorkerStatusDto> setStatus(@RequestBody WorkerStatusDto workerStatusDto) {
        if ( workerStatusDto.running() ) {
            traceWriterSupervisor.start();
        } else {
            traceWriterSupervisor.stop();
        }
        return ResponseEntity.ok(new WorkerStatusDto(traceWriterSupervisor.isRunning()));
    }

}
