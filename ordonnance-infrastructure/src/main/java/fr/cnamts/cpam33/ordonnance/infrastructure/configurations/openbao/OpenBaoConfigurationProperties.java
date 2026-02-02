package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.openbao;

import fr.cnamts.cpam33.ordonnance.infrastructure.technical.urls.PathJoiner;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vault.openbao")
public record OpenBaoConfigurationProperties(
        boolean enabled,
        String uri,
        String token,
        String namespace,
        @NotNull Paths paths,
        @NotNull Kv kv
) {
    public record Paths(String ordonnance, String trace) {}
    public record Kv(String mount, int version) {}

    public String ordonnanceReadPath() {
        return kvReadPath(paths.ordonnance());
    }

    public String traceReadPath() {
        return kvReadPath(paths.trace());
    }

    private String kvReadPath(String subPath) {
        if (kv != null && kv.version() == 2) {
            return PathJoiner.join(kv.mount(), "data", namespace, subPath);
        }
        return PathJoiner.join(kv.mount(), namespace, subPath);
    }

}
