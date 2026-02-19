package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes;

public final class Routes {

    private Routes() {}

    public static final class Patient {

        private Patient() {
            throw new UnsupportedOperationException("utility class");
        }

        public static final String ROOT = "/patients";
        public static final String IMPORT = "/import";
        public static final String IMPORT_BY_EXTERNAL_ID = IMPORT + "/{externalId}";
    }

}
