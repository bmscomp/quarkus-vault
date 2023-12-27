package io.quarkus.vault.client;

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.vault.client.api.auth.token.VaultAuthTokenCreateTokenParams;
import io.quarkus.vault.client.api.auth.token.VaultAuthTokenUpdateRoleParams;
import io.quarkus.vault.client.api.common.VaultTokenType;
import io.quarkus.vault.client.test.Random;
import io.quarkus.vault.client.test.VaultClientTest;

@VaultClientTest
public class VaultAuthTokenTest {

    @Test
    public void testListAccessors(VaultClient client) {
        var tokenApi = client.auth().token();

        var createdToken = tokenApi.create(false, null)
                .await().indefinitely();

        var accessors = tokenApi.listAccessors()
                .await().indefinitely();

        assertThat(accessors)
                .contains(createdToken.getAccessor());
    }

    @Test
    public void testCreateToken(VaultClient client) {
        var tokenApi = client.auth().token();

        var tokenInfo = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setMeta(Map.of("foo", "bar")))
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getClientToken())
                .isNotNull();
        assertThat(tokenInfo.getAccessor())
                .isNotNull();
        assertThat(tokenInfo.getPolicies())
                .contains("root");
        assertThat(tokenInfo.getTokenPolicies())
                .contains("root");
        assertThat(tokenInfo.getMetadata())
                .containsEntry("foo", "bar");
        assertThat(tokenInfo.getLeaseDuration())
                .isNotNull();
        assertThat(tokenInfo.isRenewable())
                .isFalse();
        assertThat(tokenInfo.getEntityId())
                .isEmpty();
        assertThat(tokenInfo.getTokenType())
                .isEqualTo(VaultTokenType.SERVICE);
        assertThat(tokenInfo.isOrphan())
                .isFalse();
        assertThat(tokenInfo.getMfaRequirement())
                .isNull();
        assertThat(tokenInfo.getNumUses())
                .isEqualTo(0);
    }

    @Test
    public void testCreateTokenWithRole(VaultClient client, @Random String role) {
        var tokenApi = client.auth().token();

        tokenApi.updateRole(role, new VaultAuthTokenUpdateRoleParams()
                .setAllowedPolicies(List.of("root")))
                .await().indefinitely();

        var createdToken = tokenApi.create(role, new VaultAuthTokenCreateTokenParams()
                .setRoleName(role))
                .await().indefinitely();

        var tokenInfo = tokenApi.lookup(createdToken.getClientToken())
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getRole())
                .isEqualTo(role);
    }

    @Test
    public void testLookupToken(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var policy = tokenId + "-policy";

        client.sys().policy().update(policy, """
                path "secret/*" {
                    capabilities = [ "read" ]
                }""")
                .await().indefinitely();

        var createdToken = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setId(tokenId)
                .setDisplayName("My Token")
                .setPolicies(List.of(policy))
                .setMeta(Map.of("foo", "bar"))
                .setTtl(Duration.ofMinutes(1))
                .setNumUses(5))
                .await().indefinitely();

        var tokenInfo = tokenApi.lookup(createdToken.getClientToken())
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getCreationTime())
                .isBetween(now().minusSeconds(2), now().plusSeconds(2));
        assertThat(tokenInfo.getCreationTtl())
                .isEqualTo(Duration.ofMinutes(1));
        assertThat(tokenInfo.getDisplayName())
                .isEqualTo("token-My-Token");
        assertThat(tokenInfo.getEntityId())
                .isEmpty();
        assertThat(tokenInfo.getExpireTime())
                .isBetween(now().plusSeconds(58), now().plusSeconds(62));
        assertThat(tokenInfo.getExplicitMaxTtl())
                .isEqualTo(Duration.ZERO);
        assertThat(tokenInfo.getId())
                .isEqualTo(tokenId);
        assertThat(tokenInfo.getIdentityPolicies())
                .isNull();
        assertThat(tokenInfo.getIssueTime())
                .isBetween(now().minusSeconds(2), now().plusSeconds(2));
        assertThat(tokenInfo.getMeta())
                .containsEntry("foo", "bar");
        assertThat(tokenInfo.getNumUses())
                .isEqualTo(5);
        assertThat(tokenInfo.isOrphan())
                .isFalse();
        assertThat(tokenInfo.getPath())
                .isEqualTo("auth/token/create");
        assertThat(tokenInfo.getPolicies())
                .contains(policy);
        assertThat(tokenInfo.isRenewable())
                .isTrue();
        assertThat(tokenInfo.getTtl())
                .isBetween(Duration.ofSeconds(57L), Duration.ofSeconds(60L));
        assertThat(tokenInfo.getRole())
                .isNull();
        assertThat(tokenInfo.getType())
                .isEqualTo(VaultTokenType.SERVICE);
    }

    @Test
    public void testLookupSelfToken(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var policy = tokenId + "-policy";

        client.sys().policy().update(policy, """
                path "secret/*" {
                    capabilities = [ "read" ]
                }""")
                .await().indefinitely();

        var createdToken = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setId(tokenId)
                .setDisplayName("My Token")
                .setPolicies(List.of(policy))
                .setMeta(Map.of("foo", "bar"))
                .setTtl(Duration.ofMinutes(1))
                .setNumUses(5))
                .await().indefinitely();

        var tokenClient = client.configure().clientToken(createdToken.getClientToken()).build();

        var tokenInfo = tokenClient.auth().token().lookupSelf()
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getCreationTime())
                .isBetween(now().minusSeconds(2), now().plusSeconds(2));
        assertThat(tokenInfo.getCreationTtl())
                .isEqualTo(Duration.ofMinutes(1));
        assertThat(tokenInfo.getDisplayName())
                .isEqualTo("token-My-Token");
        assertThat(tokenInfo.getEntityId())
                .isEmpty();
        assertThat(tokenInfo.getExpireTime())
                .isBetween(now().plusSeconds(58), now().plusSeconds(62));
        assertThat(tokenInfo.getExplicitMaxTtl())
                .isEqualTo(Duration.ZERO);
        assertThat(tokenInfo.getId())
                .isEqualTo(tokenId);
        assertThat(tokenInfo.getIdentityPolicies())
                .isNull();
        assertThat(tokenInfo.getIssueTime())
                .isBetween(now().minusSeconds(2), now().plusSeconds(2));
        assertThat(tokenInfo.getMeta())
                .containsEntry("foo", "bar");
        assertThat(tokenInfo.getNumUses())
                .isEqualTo(4);
        assertThat(tokenInfo.isOrphan())
                .isFalse();
        assertThat(tokenInfo.getPath())
                .isEqualTo("auth/token/create");
        assertThat(tokenInfo.getPolicies())
                .contains(policy);
        assertThat(tokenInfo.isRenewable())
                .isTrue();
        assertThat(tokenInfo.getTtl())
                .isBetween(Duration.ofSeconds(57L), Duration.ofSeconds(60L));
    }

    @Test
    public void testLookupTokenAccessor(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var createdToken = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setId(tokenId)
                .setDisplayName("My Token")
                .setMeta(Map.of("foo", "bar"))
                .setTtl(Duration.ofMinutes(1))
                .setNumUses(5))
                .await().indefinitely();

        var accessorInfo = tokenApi.lookupAccessor(createdToken.getAccessor())
                .await().indefinitely();

        assertThat(accessorInfo)
                .isNotNull();
        assertThat(accessorInfo.getAccessor())
                .isEqualTo(createdToken.getAccessor());
        assertThat(accessorInfo.getCreationTime())
                .isBetween(now().minusSeconds(2), now().plusSeconds(2));
        assertThat(accessorInfo.getCreationTtl())
                .isEqualTo(Duration.ofMinutes(1));
        assertThat(accessorInfo.getDisplayName())
                .isEqualTo("token-My-Token");
        assertThat(accessorInfo.getEntityId())
                .isEmpty();
        assertThat(accessorInfo.getExpireTime())
                .isBetween(now().plusSeconds(58), now().plusSeconds(62));
        assertThat(accessorInfo.getExplicitMaxTtl())
                .isEqualTo(Duration.ZERO);
        assertThat(accessorInfo.getId())
                .isEmpty();
        assertThat(accessorInfo.getIdentityPolicies())
                .isNull();
        assertThat(accessorInfo.getIssueTime())
                .isBetween(now().minusSeconds(2), now().plusSeconds(2));
        assertThat(accessorInfo.getMeta())
                .containsEntry("foo", "bar");
        assertThat(accessorInfo.getNumUses())
                .isEqualTo(5);
        assertThat(accessorInfo.isOrphan())
                .isFalse();
        assertThat(accessorInfo.getPath())
                .isEqualTo("auth/token/create");
        assertThat(accessorInfo.getPolicies())
                .contains("root");
        assertThat(accessorInfo.isRenewable())
                .isTrue();
        assertThat(accessorInfo.getTtl())
                .isBetween(Duration.ofSeconds(57L), Duration.ofSeconds(60L));
    }

    @Test
    public void testRenewToken(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var policy = tokenId + "-policy";

        client.sys().policy().update(policy, """
                path "secret/*" {
                    capabilities = [ "read" ]
                }""")
                .await().indefinitely();

        var createdToken = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setId(tokenId)
                .setDisplayName("My Token")
                .setPolicies(List.of(policy))
                .setMeta(Map.of("foo", "bar"))
                .setTtl(Duration.ofMinutes(1))
                .setNumUses(5))
                .await().indefinitely();

        var tokenInfo = tokenApi.renew(createdToken.getClientToken(), "30s")
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getClientToken())
                .isNotNull();
        assertThat(tokenInfo.getAccessor())
                .isNotNull();
        assertThat(tokenInfo.getPolicies())
                .contains(policy);
        assertThat(tokenInfo.getTokenPolicies())
                .contains(policy);
        assertThat(tokenInfo.getMetadata())
                .containsEntry("foo", "bar");
        assertThat(tokenInfo.getLeaseDuration())
                .isEqualTo(Duration.ofSeconds(30));
        assertThat(tokenInfo.isRenewable())
                .isTrue();
        assertThat(tokenInfo.getEntityId())
                .isEmpty();
        assertThat(tokenInfo.getTokenType())
                .isEqualTo(VaultTokenType.SERVICE);
        assertThat(tokenInfo.isOrphan())
                .isFalse();
        assertThat(tokenInfo.getMfaRequirement())
                .isNull();
        assertThat(tokenInfo.getNumUses())
                .isEqualTo(5);
    }

    @Test
    public void testRenewSelfToken(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var policy = tokenId + "-policy";

        client.sys().policy().update(policy, """
                path "secret/*" {
                    capabilities = [ "read" ]
                }""")
                .await().indefinitely();

        var createdToken = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setId(tokenId)
                .setDisplayName("My Token")
                .setPolicies(List.of(policy))
                .setMeta(Map.of("foo", "bar"))
                .setTtl(Duration.ofMinutes(1))
                .setNumUses(5))
                .await().indefinitely();

        var tokenClient = client.configure().clientToken(createdToken.getClientToken()).build();

        var tokenInfo = tokenClient.auth().token().renewSelf("30s")
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getClientToken())
                .isNotNull();
        assertThat(tokenInfo.getAccessor())
                .isNotNull();
        assertThat(tokenInfo.getPolicies())
                .contains(policy);
        assertThat(tokenInfo.getTokenPolicies())
                .contains(policy);
        assertThat(tokenInfo.getMetadata())
                .containsEntry("foo", "bar");
        assertThat(tokenInfo.getLeaseDuration())
                .isEqualTo(Duration.ofSeconds(30));
        assertThat(tokenInfo.isRenewable())
                .isTrue();
        assertThat(tokenInfo.getEntityId())
                .isEmpty();
        assertThat(tokenInfo.getTokenType())
                .isEqualTo(VaultTokenType.SERVICE);
        assertThat(tokenInfo.isOrphan())
                .isFalse();
        assertThat(tokenInfo.getMfaRequirement())
                .isNull();
        assertThat(tokenInfo.getNumUses())
                .isEqualTo(5);
    }

    @Test
    public void testRenewAccessor(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var policy = tokenId + "-policy";

        client.sys().policy().update(policy, """
                path "secret/*" {
                    capabilities = [ "read" ]
                }""")
                .await().indefinitely();

        var createdToken = tokenApi.create(false, new VaultAuthTokenCreateTokenParams()
                .setId(tokenId)
                .setDisplayName("My Token")
                .setPolicies(List.of(policy))
                .setMeta(Map.of("foo", "bar"))
                .setTtl(Duration.ofMinutes(1))
                .setNumUses(5))
                .await().indefinitely();

        var tokenInfo = tokenApi.renewAccessor(createdToken.getAccessor(), "30s")
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();
        assertThat(tokenInfo.getClientToken())
                .isNotNull();
        assertThat(tokenInfo.getAccessor())
                .isNotNull();
        assertThat(tokenInfo.getPolicies())
                .contains(policy);
        assertThat(tokenInfo.getTokenPolicies())
                .contains(policy);
        assertThat(tokenInfo.getMetadata())
                .containsEntry("foo", "bar");
        assertThat(tokenInfo.getLeaseDuration())
                .isEqualTo(Duration.ofSeconds(30));
        assertThat(tokenInfo.isRenewable())
                .isTrue();
        assertThat(tokenInfo.getEntityId())
                .isEmpty();
        assertThat(tokenInfo.getTokenType())
                .isEqualTo(VaultTokenType.SERVICE);
        assertThat(tokenInfo.isOrphan())
                .isFalse();
        assertThat(tokenInfo.getMfaRequirement())
                .isNull();
        assertThat(tokenInfo.getNumUses())
                .isEqualTo(5);
    }

    @Test
    public void testRevokeToken(VaultClient client) {
        var tokenApi = client.auth().token();

        var createdToken = tokenApi.create(false, null)
                .await().indefinitely();

        tokenApi.revoke(createdToken.getClientToken())
                .await().indefinitely();

        assertThatThrownBy(() -> tokenApi.lookup(createdToken.getClientToken())
                .await().indefinitely())
                .isInstanceOf(VaultException.class)
                .asString().contains("status=403");
    }

    @Test
    public void testRevokeSelfToken(VaultClient client) {
        var tokenApi = client.auth().token();

        var createdToken = tokenApi.create(false, null)
                .await().indefinitely();

        var tokenClient = client.configure().clientToken(createdToken.getClientToken()).build();

        tokenClient.auth().token().revokeSelf()
                .await().indefinitely();

        assertThatThrownBy(() -> client.auth().token().lookup(createdToken.getClientToken())
                .await().indefinitely())
                .isInstanceOf(VaultException.class)
                .asString().contains("status=403");
    }

    @Test
    public void testRevokeOrphanToken(VaultClient client) {
        var tokenApi = client.auth().token();

        var createdToken = tokenApi.create(null)
                .await().indefinitely();

        tokenApi.revoke(true, createdToken.getClientToken())
                .await().indefinitely();

        assertThatThrownBy(() -> tokenApi.lookup(createdToken.getClientToken())
                .await().indefinitely())
                .isInstanceOf(VaultException.class)
                .asString().contains("status=403");
    }

    @Test
    public void testRevokeAccessor(VaultClient client) {
        var tokenApi = client.auth().token();

        var createdToken = tokenApi.create(null)
                .await().indefinitely();

        tokenApi.lookupAccessor(createdToken.getAccessor())
                .await().indefinitely();

        tokenApi.revokeAccessor(createdToken.getAccessor())
                .await().indefinitely();

        var accessors = tokenApi.listAccessors()
                .await().indefinitely();

        assertThat(accessors)
                .doesNotContain(createdToken.getAccessor());
    }

    @Test
    public void testUpdateTokenRole(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var role = tokenId + "-role";
        var policy = tokenId + "-policy";

        client.auth().token().updateRole(role, new VaultAuthTokenUpdateRoleParams()
                .setAllowedPolicies(List.of(policy))
                .setDisallowedPolicies(List.of("default"))
                .setAllowedPoliciesGlob(List.of(policy + "*"))
                .setDisallowedPoliciesGlob(List.of("default*"))
                .setOrphan(true)
                .setRenewable(false)
                .setPathSuffix("foo")
                .setAllowedEntityAliases(List.of("foo", "bar"))
                .setTokenBoundCidrs(List.of("127.0.0.1/32"))
                .setTokenExplicitMaxTtl(Duration.ofMinutes(2))
                .setTokenNoDefaultPolicy(true)
                .setTokenNumUses(5)
                .setTokenPeriod(Duration.ofSeconds(90))
                .setTokenType(VaultTokenType.SERVICE))
                .await().indefinitely();

        var roleInfo = tokenApi.readRole(role)
                .await().indefinitely();

        assertThat(roleInfo)
                .isNotNull();
        assertThat(roleInfo.getAllowedEntityAliases())
                .contains("foo", "bar");
        assertThat(roleInfo.getAllowedPolicies())
                .contains(policy);
        assertThat(roleInfo.getDisallowedPolicies())
                .contains("default");
        assertThat(roleInfo.getAllowedPoliciesGlob())
                .contains(policy + "*");
        assertThat(roleInfo.getDisallowedPoliciesGlob())
                .contains("default*");
        assertThat(roleInfo.getExplicitMaxTtl())
                .isEqualTo(Duration.ZERO);
        assertThat(roleInfo.getName())
                .isEqualTo(role);
        assertThat(roleInfo.isOrphan())
                .isTrue();
        assertThat(roleInfo.getPathSuffix())
                .isEqualTo("foo");
        assertThat(roleInfo.getPeriod())
                .isEqualTo(Duration.ZERO);
        assertThat(roleInfo.isRenewable())
                .isFalse();
        assertThat(roleInfo.getTokenExplicitMaxTtl())
                .isEqualTo(Duration.ofMinutes(2));
        assertThat(roleInfo.isTokenNoDefaultPolicy())
                .isTrue();
        assertThat(roleInfo.getTokenPeriod())
                .isEqualTo(Duration.ofSeconds(90));
        assertThat(roleInfo.getTokenType())
                .isEqualTo(VaultTokenType.SERVICE);
    }

    @Test
    public void testDeleteTokenRole(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var role = tokenId + "-role";

        tokenApi.updateRole(role, null)
                .await().indefinitely();

        var tokenInfo = tokenApi.readRole(role)
                .await().indefinitely();

        assertThat(tokenInfo)
                .isNotNull();

        tokenApi.deleteRole(role)
                .await().indefinitely();

        assertThatThrownBy(() -> tokenApi.readRole(role)
                .await().indefinitely())
                .isInstanceOf(VaultException.class)
                .asString().contains("status=404");
    }

    @Test
    public void testListTokenRoles(VaultClient client, @Random String tokenId) {
        var tokenApi = client.auth().token();

        var role = tokenId + "-role";

        tokenApi.updateRole(role, null)
                .await().indefinitely();

        var roles = tokenApi.listRoles()
                .await().indefinitely();

        assertThat(roles)
                .contains(role);
    }

    @Test
    public void testTidyTokens(VaultClient client) {
        var tokenApi = client.auth().token();

        assertThatCode(() -> tokenApi.tidyTokens().await().indefinitely())
                .doesNotThrowAnyException();
    }

}
