package uk.co.mruoc.fake.jwt.token;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Builder;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Builder
public class TokenGenerator {

    private final JsonWebKeySetProvider provider;

    @Builder.Default
    private final Clock clock = Clock.systemUTC();

    @Builder.Default
    private final String defaultAudience = "default-audience";

    @Builder.Default
    private final String issuer = "https://michaelruocco.eu.auth0.com/";

    public Token generate(TokenRequest request) {
        JWTClaimsSet claims = toClaims(request);
        return Token.builder()
                .value(provider.generateToken(request.getClientId(), claims))
                .expiry(calculateExpiry(claims))
                .build();
    }

    private JWTClaimsSet toClaims(TokenRequest request) {
        String clientId = request.getClientId();
        Instant now = clock.instant();
        return new JWTClaimsSet.Builder()
                .audience(request.getAudience().orElse(defaultAudience))
                .issuer(issuer)
                .subject(String.format("%s@clients", clientId))
                .claim("azp", clientId)
                .claim("gty", "client-credentials")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .build();
    }

    private Duration calculateExpiry(JWTClaimsSet claims) {
        return Duration.between(claims.getIssueTime().toInstant(), claims.getExpirationTime().toInstant());
    }

}
