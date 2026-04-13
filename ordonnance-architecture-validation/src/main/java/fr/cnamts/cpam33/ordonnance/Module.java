package fr.cnamts.cpam33.ordonnance;

public enum Module {

    DOMAIN(
            "fr.cnamts.cpam33.ordonnance.domain",
            "..domain.."),
    INFRASTRUCTURE(
            "fr.cnamts.cpam33.ordonnance.infrastructure",
            "..infrastructure.."),
    APPLICATION(
            "fr.cnamts.cpam33.ordonnance.application",
            "..application.."),
    APPLICATION_USE_CASES(
            "fr.cnamts.cpam33.ordonnance.application.usecases",
            "..application.usecases.."
    ),
    APPLICATION_SERVICES(
            "fr.cnamts.cpam33.ordonnance.application.services",
            "..application.services.."
    ),
    DOMAIN_MODELS(
            "fr.cnamts.cpam33.ordonnance.domain.models",
            "..domain.models.."
    ),
    APPLICATION_VIEWS(
            "fr.cnamts.cpam33.ordonnance.application.views",
            "..application.views.."
    ),
    APPLICATION_COMMANDS(
            "fr.cnamts.cpam33.ordonnance.application.commands",
            "..application.commands.."
    ),
    APPLICATION_QUERIES(
            "fr.cnamts.cpam33.ordonnance.application.queries",
            "..application.queries.."
    ),
    APPLICATION_FILTERS(
                "fr.cnamts.cpam33.ordonnance.application.filters",
                "..application.filters.."
    ),
    INFRASTRUCTURE_ADMIN(
            "fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.admin",
            "..in.adapters.controllers.admin.."
    );

    private String packageName;
    private String archPackageName;

    private Module(String packageName, String archPackageName) {
        this.packageName = packageName;
        this.archPackageName = archPackageName;
    }

    public String getPackage() {
        return packageName;
    }

    public String getArchPackage() {
        return archPackageName;
    }

}
