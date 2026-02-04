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

    public String ordonnancePath() {
        return kvReadPath(paths.ordonnance());
    }

    public String tracePath() {
        return kvReadPath(paths.trace());
    }

    private String kvReadPath(String path) {
        if ( kv == null ) {
            throw new IllegalStateException("KV engine non initialisé");
        }
        String[] parts = (kv.version() == 2)
                ? new String[]{ kv.mount(), "data", namespace, path }
                : new String[]{ kv.mount(), namespace, path };
        return PathJoiner
                .join(parts)
                .orElseThrow(() -> new IllegalStateException("KV read path vide"));
    }

}
