package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Forme;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.VoieAdministration;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.MedicamentId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MedicamentRowMapper {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentRowMapper.class);

    public Medicament map(Map<String, Object> rows) {
        String codeSq = Row.getString(rows, "sp_code_sq_pk");
        MedicamentId medicamentId = new MedicamentId(codeSq);
        String nom = Row.getString(rows, "sp_nom");
        String nomLong = Row.getString(rows,"sp_nom_long");
        String cdfNom = Row.getNullableString(rows, "cdf_nom");
        String catcCode = Row.getNullableString(rows, "sp_catc_code_fk");
        String cipUcd = Row.getNullableString(rows, "sp_cipucd");
        Forme forme = new Forme(Row.getString(rows, "forme"));
        VoieAdministration voieAdministration = new VoieAdministration(Row.getNullableString(rows, "voie"));
        String atu = Row.getNullableString(rows, "atu");
        String t2a = Row.getNullableString(rows, "t2a");
        List<Posologie> posologies = List.of();
        logger.trace("map medicamentId={}", medicamentId);
        return new Medicament(
                medicamentId,
                nom,
                nomLong,
                cdfNom,
                catcCode,
                cipUcd,
                forme,
                atu,
                t2a,
                voieAdministration,
                posologies
        );
    }

}
