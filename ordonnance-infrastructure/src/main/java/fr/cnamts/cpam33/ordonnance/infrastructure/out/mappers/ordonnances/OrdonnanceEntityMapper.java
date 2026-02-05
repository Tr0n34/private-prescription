package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.EntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins.MedecinEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.PatientEntityMapper;
import org.mapstruct.Mapper;

import java.util.Optional;


@Mapper(componentModel = "spring", uses = {
        PatientEntityMapper.class,
        MedecinEntityMapper.class,
        PrescriptionEntityMapper.class,
        OrdonnanceIdMapper.class
})
public abstract class OrdonnanceEntityMapper implements EntityMapper<OrdonnanceEntity, Ordonnance> {

    protected PatientEntityMapper patientMapper;
    protected MedecinEntityMapper medecinMapper;
    protected PrescriptionEntityMapper prescriptionMapper;
    protected OrdonnanceIdMapper ordonnanceIdMapper;

    @Override
    public Ordonnance toDomain(OrdonnanceEntity entity) {
        return Optional.ofNullable(entity)
                .map(e -> Ordonnance.of(
                        ordonnanceIdMapper.toDomain(e.getOrdonnanceId()),
                        patientMapper.toDomain(e.getPatient()),
                        medecinMapper.toDomain(e.getMedecin()),
                        e.getPrescriptions().stream()
                                .map(prescriptionMapper::toDomain)
                                .toList()
                ))
                .orElse(null);
    }

    @Override
    public OrdonnanceEntity toEntity(Ordonnance domain) {
        return Optional.ofNullable(domain)
                .map(d -> {
                    OrdonnanceEntity entity = new OrdonnanceEntity();
                    entity.setOrdonnanceId(ordonnanceIdMapper.toEntity(d.ordonnanceId()));
                    entity.setPatient(patientMapper.toEntity(d.patient()));
                    entity.setMedecin(medecinMapper.toEntity(d.medecin()));
                    entity.setPrescriptions(d.prescriptions().stream()
                            .map(prescriptionMapper::toEntity)
                            .toList());
                    return entity;
                })
                .orElse(null);
    }

}