package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.IEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "prescription")
public class PrescriptionEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordonnance_id", nullable = false)
    private OrdonnanceEntity ordonnance;

    @Column(name = "medicament")
    private String medicament;

}
