package io.quarkus.vault.sys;

import java.util.Map;

/**
 * Options for enabling a new secret engine.
 */
public class EnableEngineOptions {

    /**
     * Default lease duration.
     */
    public String defaultLeaseTimeToLive;

    /**
     * Max lease duration.
     */
    public String maxLeaseTimeToLive;

    /**
     * Engine specific mount options
     */
    public Map<String, String> options;

    public void setDefaultLeaseTimeToLive(String defaultLeaseTimeToLive) {
        this.defaultLeaseTimeToLive = defaultLeaseTimeToLive;
    }

    public void setMaxLeaseTimeToLive(String maxLeaseTimeToLive) {
        this.maxLeaseTimeToLive = maxLeaseTimeToLive;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
