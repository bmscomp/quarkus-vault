package io.quarkus.vault.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.quarkus.vault.VaultPKISecretEngine;
import io.quarkus.vault.VaultPKISecretEngineFactory;
import io.quarkus.vault.VaultPKISecretReactiveEngine;
import io.quarkus.vault.runtime.client.VaultClient;
import io.quarkus.vault.runtime.client.secretengine.VaultInternalPKISecretEngine;

@ApplicationScoped
public class VaultPKIManagerFactory implements VaultPKISecretEngineFactory {

    static final String PKI_ENGINE_NAME = "pki";

    @Inject
    VaultClient vaultClient;
    @Inject
    VaultAuthManager vaultAuthManager;
    @Inject
    VaultInternalPKISecretEngine vaultInternalPKISecretEngine;

    @Override
    public VaultPKISecretEngine engine(String mount) {
        return new VaultPKISecretEngine(reactiveEngine(mount));
    }

    @Override
    public VaultPKISecretReactiveEngine reactiveEngine(String mount) {
        return new VaultPKIManager(vaultClient, mount, vaultAuthManager, vaultInternalPKISecretEngine);
    }
}
