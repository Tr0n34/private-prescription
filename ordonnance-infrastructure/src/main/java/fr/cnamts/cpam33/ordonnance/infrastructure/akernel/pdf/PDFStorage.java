package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.pdf;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;

public interface PDFStorage {

    void save(String key, byte[] pdfBytes) throws InfrastructureException;

    byte[] load(String key) throws InfrastructureException;

    boolean exists(String key) throws InfrastructureException;

}
