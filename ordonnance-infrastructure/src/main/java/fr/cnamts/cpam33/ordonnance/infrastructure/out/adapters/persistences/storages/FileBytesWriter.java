package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.storages;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.OpsError;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

public final class FileBytesWriter {

    private FileBytesWriter() {}

    public static <E extends InfrastructureException> void write(
            Path path,
            byte[] bytes,
            OpsError.ExceptionFactory<E> dirError,
            OpsError.ExceptionFactory<E> writeError) {
        Path parent = path.getParent();
        if ( parent != null ) {
            OpsError.io(() -> Files.createDirectories(parent), dirError);
        }
        OpsError.io(() -> Files.write(path, bytes, CREATE, TRUNCATE_EXISTING, WRITE), writeError);
    }

}
