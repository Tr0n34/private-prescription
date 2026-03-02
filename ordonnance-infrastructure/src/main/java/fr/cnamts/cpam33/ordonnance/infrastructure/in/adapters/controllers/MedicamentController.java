package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.medicaments.ListerDetailsMedicamentUseCase;
import fr.cnamts.cpam33.ordonnance.application.views.medicaments.MedicamentView;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.filters.MedicamentFilter;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.MedicamentApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PageableQueryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    private final ListerDetailsMedicamentUseCase listerDetailsMedicamentUseCase;
    private final MedicamentApiMapper medicamentApiMapper;
    private final PageableQueryMapper pageableQueryMapper;


    public MedicamentController(ListerDetailsMedicamentUseCase listerDetailsMedicamentUseCase,
                                MedicamentApiMapper medicamentApiMapper,
                                PageableQueryMapper pageableQueryMapper) {
        this.listerDetailsMedicamentUseCase = listerDetailsMedicamentUseCase;
        this.medicamentApiMapper = medicamentApiMapper;
        this.pageableQueryMapper = pageableQueryMapper;
    }

    @GetMapping
    public ResponseEntity<Page<MedicamentView>> getSpeTheDetail(
            MedicamentFilter medicamentFilter,
            @RequestHeader("userId") String userId,
            @PageableDefault(size = 10) @SortDefault(sort = "nom") Pageable pageable
    ) {
        var query = medicamentApiMapper.toQuery(medicamentFilter, userId);
        var pageRequest = pageableQueryMapper.toQuery(pageable);
        var result = listerDetailsMedicamentUseCase.execute(query, pageRequest);
        return ResponseEntity.ok(new PageImpl<>(
                result.content(),
                pageable,
                result.totalElements()
        ));
    }

}
