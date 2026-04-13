package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.IEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ordonnance")
public class OrdonnanceEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrdonnanceIdEntity ordonnanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "")
    private MedecinEntity medecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @OneToMany(mappedBy = "ordonnance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionEntity> prescriptions;


    public Long getId() {
        return id;
    }

    public OrdonnanceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public OrdonnanceIdEntity getOrdonnanceId() {
        return ordonnanceId;
    }

    public OrdonnanceEntity setOrdonnanceId(OrdonnanceIdEntity ordonnanceId) {
        this.ordonnanceId = ordonnanceId;
        return this;
    }

    public MedecinEntity getMedecin() {
        return medecin;
    }

    public OrdonnanceEntity setMedecin(MedecinEntity medecin) {
        this.medecin = medecin;
        return this;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public OrdonnanceEntity setPatient(PatientEntity patient) {
        this.patient = patient;
        return this;
    }

    public List<PrescriptionEntity> getPrescriptions() {
        return prescriptions;
    }

    public OrdonnanceEntity setPrescriptions(List<PrescriptionEntity> prescriptions) {
        this.prescriptions = prescriptions;
        return this;
    }

}
