package io.quarkus.vault.sys;

import java.util.List;

public class VaultInit {

    private final List<String> keys;
    private final List<String> keysBase64;
    private final String rootToken;

    public VaultInit(List<String> keys, List<String> keysBase64, String rootToken) {
        this.keys = keys;
        this.keysBase64 = keysBase64;
        this.rootToken = rootToken;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getKeysBase64() {
        return keysBase64;
    }

    public String getRootToken() {
        return rootToken;
    }

    @Override
    public String toString() {
        return "VaultInit{" + keys.size() + " keys}";
    }
}
