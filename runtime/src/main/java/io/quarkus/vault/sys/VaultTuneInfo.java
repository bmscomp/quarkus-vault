package io.quarkus.vault.sys;

public class VaultTuneInfo {

    private String description;
    private Long defaultLeaseTimeToLive;
    private Long maxLeaseTimeToLive;
    private Boolean forceNoCache;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDefaultLeaseTimeToLive() {
        return defaultLeaseTimeToLive;
    }

    public void setDefaultLeaseTimeToLive(Long defaultLeaseTimeToLive) {
        this.defaultLeaseTimeToLive = defaultLeaseTimeToLive;
    }

    public Long getMaxLeaseTimeToLive() {
        return maxLeaseTimeToLive;
    }

    public void setMaxLeaseTimeToLive(Long maxLeaseTimeToLive) {
        this.maxLeaseTimeToLive = maxLeaseTimeToLive;
    }

    public Boolean getForceNoCache() {
        return forceNoCache;
    }

    public void setForceNoCache(Boolean forceNoCache) {
        this.forceNoCache = forceNoCache;
    }
}
