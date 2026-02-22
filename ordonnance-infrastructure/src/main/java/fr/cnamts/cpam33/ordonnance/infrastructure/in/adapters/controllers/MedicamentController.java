package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.medicaments.ListerDetailsMedicamentUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageRequest;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageResult;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.filters.MedicamentFilter;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medicaments.MedicamentDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.MedicamentApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PageableApiMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final ListerDetailsMedicamentUseCase listerDetailsMedicamentUseCase;
    private final MedicamentApiMapper medicamentApiMapper;
    private final PageableApiMapper pageableApiMapper;

    public MedicamentController(ListerDetailsMedicamentUseCase listerDetailsMedicamentUseCase,
                                MedicamentApiMapper medicamentApiMapper,
                                PageableApiMapper pageableApiMapper) {
        this.listerDetailsMedicamentUseCase = listerDetailsMedicamentUseCase;
        this.medicamentApiMapper = medicamentApiMapper;
        this.pageableApiMapper = pageableApiMapper;
    }

    @GetMapping
    public ResponseEntity<Page<MedicamentDto>> getSpeTheDetail(
            MedicamentFilter medicamentFilter,
            @RequestHeader("userId") String userId,
            @PageableDefault(size = 10) @SortDefault(sort = "nom") Pageable pageable
    ) {
        var query = new ListerMedicamentByCodeIdQuery(
                medicamentFilter.codeSp(),
                medicamentFilter.varType(),
                new UtilisateurId(userId)
        );
        var pageRequest = pageableApiMapper.toDomain(pageable);
        var result = listerDetailsMedicamentUseCase.execute(query, pageRequest);
        var dtoResult = result.map(medicamentApiMapper::toDto);
        return ResponseEntity.ok(new PageImpl<>(
                dtoResult.content(),
                pageable,
                dtoResult.totalElements()
        ));
    }

}
