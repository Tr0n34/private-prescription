package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.openbao;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.enums.VaultExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.urls.PathJoiner;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;

@ConfigurationProperties(prefix = "vault.openbao")
public record OpenBaoConfigurationProperties(
        boolean enabled,
        String uri,
        String token,
        String namespace,
        @NotNull Paths paths,
        @NotNull Kv kv
) {

    private static final Logger logger = LoggerFactory.getLogger(OpenBaoConfigurationProperties.class.getName());

    public static final int KV_VERSION_1 = 1;
    public static final int KV_VERSION_2 = 2;

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
            throw new InfrastructureException(VaultExceptionCode.TECH_VAULT_KV_NOT_READY);
        }
        String[] parts = ( kv.version() == KV_VERSION_2 )
                ? new String[]{ kv.mount(), "data", namespace, path }
                : new String[]{ kv.mount(), namespace, path };
        logger.debug("kvReadPath: [{}]", Arrays.toString(parts));
        return PathJoiner
                .join(parts)
                .orElseThrow(() -> new InfrastructureException(VaultExceptionCode.TECH_VAULT_KV_READ_PATH_EMPTY));
    }

}
