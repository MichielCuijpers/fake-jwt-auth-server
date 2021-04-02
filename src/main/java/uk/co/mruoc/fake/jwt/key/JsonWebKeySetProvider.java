package uk.co.mruoc.fake.jwt.key;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.fake.jwt.FakeJwtException;
import uk.co.mruoc.fake.jwt.key.rsa.RsaKeyProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JsonWebKeySetProvider {

    private final Collection<JsonWebKeyProvider> keyProviders;

    public JsonWebKeySetProvider() {
        this(new RsaKeyProvider());
    }

    public JsonWebKeySetProvider(JsonWebKeyProvider... keyProviders) {
        this(Arrays.asList(keyProviders));
    }

    public JWKSet getKeySet() {
        return new JWKSet(keyProviders.stream()
                .map(JsonWebKeyProvider::getKey)
                .collect(Collectors.toList())
        );
    }

    public String generateToken(String clientId, JWTClaimsSet claims) {
        return keyProviders.stream()
                .filter(provider -> provider.supports(clientId))
                .map(provider -> provider.generateToken(claims))
                .findFirst()
                .orElseThrow(() -> new FakeJwtException(String.format("key provider not found for client id %s", clientId)));
    }

}
