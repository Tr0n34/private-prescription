package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.medicaments.ListerMedicamentUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.filters.MedicamentFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final ListerMedicamentUseCase listerMedicamentUseCase;

    public MedicamentController(ListerMedicamentUseCase listerMedicamentUseCase) {
        this.listerMedicamentUseCase = listerMedicamentUseCase;
    }

    @GetMapping
    public List<Medicament> getSpeTheDetail(
            MedicamentFilter medicamentFilter,
            @RequestHeader("userId") String userId
    ) {
        var query = new ListerMedicamentByCodeIdQuery(
                medicamentFilter.codeSp(),
                medicamentFilter.varType(),
                new UtilisateurId(userId)
        );

        return listerMedicamentUseCase.execute(query)
                .stream()
                //.map(mapper::toDto)
                .toList();
    }

}
