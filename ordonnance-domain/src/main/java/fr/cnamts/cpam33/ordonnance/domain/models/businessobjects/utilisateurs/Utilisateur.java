package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

public interface Utilisateur {

    UtilisateurId utilisateurId();

    Nom nom();

    Prenom prenom();

}
