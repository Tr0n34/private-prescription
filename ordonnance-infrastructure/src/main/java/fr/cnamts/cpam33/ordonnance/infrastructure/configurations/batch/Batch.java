package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch;

public enum Batch {

    ERROR("errorCatalog");

    private String name;

    Batch(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

}
