package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.Fixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Rpps;

public class MedecinFixtures extends Fixtures {

    public static Medecin medecinValide() {
        return new Medecin(
                new MedecinId("1234567891234", rppsValide()),
                new Nom("Dupond"),
                new Prenom("Felix"));
    }

    public static Rpps rppsValide() {
        return new Rpps("11052317852");
    }

}
