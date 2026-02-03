package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.pdf;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;

public interface PDFStorage {

    void save(String key, byte[] pdfBytes) throws InfrastructureException;

    byte[] load(String key) throws InfrastructureException;

    boolean exists(String key) throws InfrastructureException;

}
