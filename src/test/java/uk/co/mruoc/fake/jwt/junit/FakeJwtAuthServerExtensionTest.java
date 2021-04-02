package uk.co.mruoc.fake.jwt.junit;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import uk.co.mruoc.fake.jwt.authserver.DefaultFakeJwtAuthServerConfig;
import uk.co.mruoc.fake.jwt.authserver.FakeJwtAuthServerConfig;
import uk.co.mruoc.fake.jwt.token.Token;
import uk.co.mruoc.fake.jwt.token.TokenRequest;
import uk.mruoc.fake.jwt.FakeJwtClient;

import java.sql.Date;
import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class FakeJwtAuthServerExtensionTest {

    private static final Instant NOW = Instant.parse("2021-04-02T18:18:40Z");
    private static final Clock CLOCK = Clock.fixed(NOW, ZoneId.systemDefault());
    private static final FakeJwtAuthServerConfig CONFIG = new DefaultFakeJwtAuthServerConfig(CLOCK);

    @RegisterExtension
    public static final FakeJwtAuthServerExtension SERVER = new FakeJwtAuthServerExtension(CONFIG);

    private final FakeJwtClient client = new FakeJwtClient(SERVER.localUri());

    @Test
    void shouldReturnValidTokenDirectlyFromExtension() throws java.text.ParseException, JOSEException {
        TokenRequest request = TokenRequest.builder()
                .clientId("test-client-id-1")
                .build();

        Token token = SERVER.generateToken(request);

        SignedJWT signedJwt = SignedJWT.parse(token.getValue());
        JWSVerifier verifier = getRsaJwsVerifierFromKeySet();
        assertThat(signedJwt.verify(verifier)).isTrue();
        assertThat(token.getExpiry()).isEqualTo(Duration.ofHours(1));
    }

    @Test
    void shouldReturnTokenThatIsValidAgainstKeySet() throws JOSEException {
        TokenRequest request = TokenRequest.builder()
                .clientId("test-client-id-2")
                .build();

        SignedJWT signedJwt = client.getToken(request);

        JWSVerifier verifier = getRsaJwsVerifierFromKeySet();
        assertThat(signedJwt.verify(verifier)).isTrue();
    }

    @Test
    void shouldPopulateDefaultClaimsInToken() throws ParseException {
        TokenRequest request = TokenRequest.builder()
                .clientId("test-client-id-3")
                .build();

        SignedJWT signedJwt = client.getToken(request);

        JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
        assertThat(claims.getSubject()).isEqualTo(String.format("%s@clients", request.getClientId()));
        assertThat(claims.getIssuer()).isEqualTo("https://michaelruocco.eu.auth0.com/");
        assertThat(claims.getIssueTime()).isEqualTo(Date.from(NOW));
        assertThat(claims.getExpirationTime()).isEqualTo(Date.from(NOW.plus(1, ChronoUnit.HOURS)));
    }

    @Test
    void shouldReturnTokenWithAudienceIfProvidedInRequest() throws ParseException {
        String audience = "test-audience";
        TokenRequest request = TokenRequest.builder()
                .clientId("test-client-id-4")
                .audience(audience)
                .build();

        SignedJWT signedJwt = client.getToken(request);

        JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
        assertThat(claims.getAudience()).containsExactly(audience);
    }

    @Test
    void shouldReturnTokenWithDefaultAudienceIfNotProvidedInRequest() throws ParseException {
        TokenRequest request = TokenRequest.builder()
                .clientId("test-client-id-5")
                .build();

        SignedJWT signedJwt = client.getToken(request);

        JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
        assertThat(claims.getAudience()).containsExactly("default-audience");
    }

    private JWSVerifier getRsaJwsVerifierFromKeySet() throws JOSEException {
        JWKSet jwkSet = client.getKeySet();
        RSAKey rsaKey = jwkSet.getKeyByKeyId("default-rsa-key-id").toRSAKey();
        return new RSASSAVerifier(rsaKey);
    }

}
