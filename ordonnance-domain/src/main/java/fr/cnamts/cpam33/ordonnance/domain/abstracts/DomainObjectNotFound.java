package fr.cnamts.cpam33.ordonnance.domain.abstracts;

public class DomainObjectNotFound extends DomainException {

    public DomainObjectNotFound(ExceptionCode code) {
        super(code);
    }

}
