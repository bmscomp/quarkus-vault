package io.quarkus.vault.secrets.totp;

import java.util.Objects;

public class KeyConfiguration {

    private final String accountName;
    private final String algorithm;
    private final int digits;
    private final String issuer;
    private final int period;

    public KeyConfiguration(String accountName,
            String algorithm,
            int digits,
            String issuer,
            int period) {
        this.accountName = accountName;
        this.algorithm = algorithm;
        this.digits = digits;
        this.issuer = issuer;
        this.period = period;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getDigits() {
        return digits;
    }

    public String getIssuer() {
        return issuer;
    }

    public int getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final KeyConfiguration that = (KeyConfiguration) o;
        return digits == that.digits &&
                period == that.period &&
                accountName.equals(that.accountName) &&
                Objects.equals(algorithm, that.algorithm) &&
                issuer.equals(that.issuer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, issuer);
    }

    @Override
    public String toString() {
        return "KeyConfiguration{" + "accountName='" + accountName + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", digits=" + digits +
                ", issuer='" + issuer + '\'' +
                ", period=" + period +
                '}';
    }
}
