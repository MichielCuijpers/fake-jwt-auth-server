package uk.co.mruoc.fake.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.fake.jwt.token.TokenRequest;
import uk.mruoc.fake.jwt.FakeJwtClient;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.webcompere.systemstubs.SystemStubs.tapSystemErr;

class MainTest {

    @Test
    void shouldPrintUsageIfArgumentsAreInvalid() throws Exception {
        String[] args = new String[]{"-p", "abc"};

        String output = tapSystemErr(() -> Main.main(args));

        assertThat(output).endsWith(
                String.format(
                        "Usage: fake-jwt-auth-server [options]%n" +
                                "  Options:%n" +
                                "    --audience, -a%n" +
                                "      The audience to use when generating jwt if not supplied in request%n" +
                                "      Default: default-audience%n" +
                                "    --issuer, -i%n" +
                                "      The issuer to use when generating jwt tokens%n" +
                                "      Default: default-issuer%n" +
                                "    --port, -p%n" +
                                "      The port to run the server on, 0 will select a random free port%n" +
                                "      Default: 0%n%n"
                )
        );
    }

    @Test
    void shouldStartServerIfArgumentsAreValid() throws Exception {
        String[] args = new String[]{"-p", "0", "-i", "test-issuer", "-a", "test-audience"};

        String output = tapSystemErr(() -> Main.main(args));

        assertThat(output).contains("server running at http://localhost:");
    }

    @Test
    void shouldReturnTokenWithIssueAndAudienceFromCommandLineArguments() throws Exception {
        String port = "8090";
        String issuer = "test-issuer";
        String audience = "test-audience";
        String[] args = new String[]{"-p", port, "-i", issuer, "-a", audience};

        Main.main(args);

        FakeJwtClient client = new FakeJwtClient(String.format("http://localhost:%s", port));
        JWSVerifier verifier = VerifierFactory.getRsaJwsVerifierFromKeySet(client);

        TokenRequest request = TokenRequest.builder().clientId("test-client-id").build();
        SignedJWT signedJwt = client.getToken(request);
        assertThat(signedJwt.verify(verifier)).isTrue();

        JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
        assertThat(claims.getAudience()).containsExactly(audience);
        assertThat(claims.getIssuer()).isEqualTo(issuer);
    }

}
