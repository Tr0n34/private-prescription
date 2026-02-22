package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.medicaments.ListerDetailsMedicamentUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.filters.MedicamentFilter;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medicaments.MedicamentDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.MedicamentApiMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final ListerDetailsMedicamentUseCase listerDetailsMedicamentUseCase;
    private final MedicamentApiMapper medicamentApiMapper;


    public MedicamentController(ListerDetailsMedicamentUseCase listerDetailsMedicamentUseCase,
                                MedicamentApiMapper medicamentApiMapper) {
        this.listerDetailsMedicamentUseCase = listerDetailsMedicamentUseCase;
        this.medicamentApiMapper = medicamentApiMapper;
    }

    @GetMapping
    public ResponseEntity<List<MedicamentDto>> getSpeTheDetail(
            MedicamentFilter medicamentFilter,
            @RequestHeader("userId") String userId
    ) {
        var query = new ListerMedicamentByCodeIdQuery(
                medicamentFilter.codeSp(),
                medicamentFilter.varType(),
                new UtilisateurId(userId)
        );
        return ResponseEntity.ok(listerDetailsMedicamentUseCase.execute(query)
                .stream()
                .map(medicamentApiMapper::toDto)
                .toList());
    }

}
