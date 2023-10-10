package io.quarkus.vault.pki;

import java.util.List;

/**
 * Options for generating a certificate issued by the engine's CA.
 */
public class GenerateCertificateOptions {

    /**
     * Specifies Common Name (CN) of the certificate's subject.
     */
    private String subjectCommonName;

    /**
     * Specifies Subject Alternative Names.
     * <p>
     * These can be host names or email addresses; they will be parsed into their respective fields.
     */
    private List<String> subjectAlternativeNames;

    /**
     * Flag determining if the Common Name (CN) of the subject will be included
     * by default in the Subject Alternative Names of issued certificates.
     */
    private Boolean excludeCommonNameFromSubjectAlternativeNames;

    /**
     * Specifies IP Subject Alternative Names.
     */
    private List<String> ipSubjectAlternativeNames;

    /**
     * Specifies URI Subject Alternative Names.
     */
    private List<String> uriSubjectAlternativeNames;

    /**
     * Specifies custom OID/UTF8-string Subject Alternative Names.
     * <p>
     * The format is the same as OpenSSL: <oid>;<type>:<value> where the only current valid type is UTF8. This
     * can be a comma-delimited list or a JSON string slice. Must match allowed_other_sans specified on the role.
     */
    private List<String> otherSubjectAlternativeNames;

    /**
     * Specifies request time-to-live. If not specified, the role's TTL will be used.
     * <p>
     * Value is specified as a string duration with time suffix. Hour is the largest supported suffix.
     */
    private String timeToLive;

    /**
     * Specifies returned format of certificate & private key data. If unspecified it defaults
     * to {@link DataFormat#PEM}
     */
    private DataFormat format;

    /**
     * Specifies encoding of private key data. If unspecified it defaults to {@link PrivateKeyEncoding#PKCS8}.
     */
    private PrivateKeyEncoding privateKeyEncoding;

    public void setSubjectCommonName(String subjectCommonName) {
        this.subjectCommonName = subjectCommonName;
    }

    public void setSubjectAlternativeNames(List<String> subjectAlternativeNames) {
        this.subjectAlternativeNames = subjectAlternativeNames;
    }

    public void setExcludeCommonNameFromSubjectAlternativeNames(Boolean excludeCommonNameFromSubjectAlternativeNames) {
        this.excludeCommonNameFromSubjectAlternativeNames = excludeCommonNameFromSubjectAlternativeNames;
    }

    public void setIpSubjectAlternativeNames(List<String> ipSubjectAlternativeNames) {
        this.ipSubjectAlternativeNames = ipSubjectAlternativeNames;
    }

    public void setUriSubjectAlternativeNames(List<String> uriSubjectAlternativeNames) {
        this.uriSubjectAlternativeNames = uriSubjectAlternativeNames;
    }

    public void setOtherSubjectAlternativeNames(List<String> otherSubjectAlternativeNames) {
        this.otherSubjectAlternativeNames = otherSubjectAlternativeNames;
    }

    public void setTimeToLive(String timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setFormat(DataFormat format) {
        this.format = format;
    }

    public void setPrivateKeyEncoding(PrivateKeyEncoding privateKeyEncoding) {
        this.privateKeyEncoding = privateKeyEncoding;
    }

    public String getSubjectCommonName() {
        return subjectCommonName;
    }

    public List<String> getSubjectAlternativeNames() {
        return subjectAlternativeNames;
    }

    public Boolean getExcludeCommonNameFromSubjectAlternativeNames() {
        return excludeCommonNameFromSubjectAlternativeNames;
    }

    public List<String> getIpSubjectAlternativeNames() {
        return ipSubjectAlternativeNames;
    }

    public List<String> getUriSubjectAlternativeNames() {
        return uriSubjectAlternativeNames;
    }

    public List<String> getOtherSubjectAlternativeNames() {
        return otherSubjectAlternativeNames;
    }

    public String getTimeToLive() {
        return timeToLive;
    }

    public DataFormat getFormat() {
        return format;
    }

    public PrivateKeyEncoding getPrivateKeyEncoding() {
        return privateKeyEncoding;
    }
}
