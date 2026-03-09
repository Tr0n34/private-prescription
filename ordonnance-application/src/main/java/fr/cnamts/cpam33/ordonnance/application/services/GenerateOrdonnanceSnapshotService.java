package fr.cnamts.cpam33.ordonnance.application.services;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.ordonnances.OrdonnanceSnapshotFactory;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.ordonnances.OrdonnanceSnapshotGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceRepository;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceSnapshotWriter;
import org.springframework.stereotype.Service;

@Service
public class GenerateOrdonnanceSnapshotService implements OrdonnanceSnapshotGenerator {

    private final OrdonnanceRepository ordonnanceRepository;
    private final OrdonnanceSnapshotWriter ordonnanceSnapshotWriter;

    public GenerateOrdonnanceSnapshotService(OrdonnanceRepository ordonnanceRepository, OrdonnanceSnapshotWriter ordonnanceSnapshotWriter) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.ordonnanceSnapshotWriter = ordonnanceSnapshotWriter;
    }

    @Override
    public OrdonnanceSnapshot snapshotOf(OrdonnanceId ordonnanceId, Signature signature) {
        Ordonnance ordonnance = ordonnanceRepository.findById(ordonnanceId).orElseThrow(
                () -> new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING)
        );
        OrdonnanceSnapshot snapshot = OrdonnanceSnapshotFactory.create(ordonnance, signature);
        ordonnanceSnapshotWriter.writeSnapshot(snapshot);
        return snapshot;
    }

}
