package fr.cnamts.cpam33.ordonnance;

public enum TechPackage {

    JDK("java.."),
    SPRING("org.springframework.."),
    SLF_4J("org.slf4j.."),
    JAKARTA("jakarta..");

    private String packageName;

    TechPackage(String packageName) {
        this.packageName = packageName;
    }

    public String getPackage() {
        return packageName;
    }

}
