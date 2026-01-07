package fr.cnamts.cpam33.ordonnance;

public enum ClassSuffix {

    REPOSITORY("Repository"),
    REPOSITORY_ADAPTER("RepositoryAdapter"),
    JPA_REPOSITORY("JpaRepository"),
    REPOSITORY_IMPL("RepositoryImpl"),
    CONTROLLER("Controller"),
    MAPPER("Mapper"),
    DTO("Dto");

    private final String suffix;

    ClassSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
