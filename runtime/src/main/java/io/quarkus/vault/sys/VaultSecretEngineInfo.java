package io.quarkus.vault.sys;

import java.util.Map;

public class VaultSecretEngineInfo {

    private String description;

    private Boolean externalEntropyAccess;

    private Boolean local;

    private Boolean sealWrap;

    private String type;

    private Map<String, Object> options;

    public Boolean getExternalEntropyAccess() {
        return externalEntropyAccess;
    }

    public Boolean getLocal() {
        return local;
    }

    public Boolean getSealWrap() {
        return sealWrap;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExternalEntropyAccess(Boolean externalEntropyAccess) {
        this.externalEntropyAccess = externalEntropyAccess;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public void setSealWrap(Boolean sealWrap) {
        this.sealWrap = sealWrap;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
