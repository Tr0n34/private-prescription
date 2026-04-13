package fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.DomainExceptionCode;

public final class NotFound {

    public static final String OBJECT_TYPE = "objectType";
    public static final String ID = "id";

    private NotFound() {
        throw new UnsupportedOperationException("Use static factory methods instead.");
    }

    public static DomainException of(String objectType, Object id) {
        return new DomainException(
                DomainExceptionCode.NOT_FOUND,
                ErrorPlaceHolders.of(OBJECT_TYPE, objectType, ID, id)
        );
    }

}
