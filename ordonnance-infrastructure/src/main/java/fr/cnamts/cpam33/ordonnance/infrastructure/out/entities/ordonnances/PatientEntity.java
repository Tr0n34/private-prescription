package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.IEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
public class PatientEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "patient_id", nullable = false, unique = true)
    private String patientId;
    @Column(name = "external_id", nullable = true, unique = true)
    private String externalId;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column
    private LocalDate dateNaissance;

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public PatientEntity setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public PatientEntity setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getPrenom() {
        return prenom;
    }

    public PatientEntity setPrenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public PatientEntity setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public PatientEntity setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

}
