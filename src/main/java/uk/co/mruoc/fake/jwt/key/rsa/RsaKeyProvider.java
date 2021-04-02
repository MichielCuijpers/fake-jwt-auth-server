package uk.co.mruoc.fake.jwt.key.rsa;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.fake.jwt.FakeJwtException;
import uk.co.mruoc.fake.jwt.key.JsonWebKeyProvider;

@RequiredArgsConstructor
public class RsaKeyProvider implements JsonWebKeyProvider {

    private static final String DEFAULT_ID = "default-rsa-key-id";

    private final RSAKey rsaKey;
    private final String id;
    private final JWSAlgorithm algorithm;

    public RsaKeyProvider() {
        this(DEFAULT_ID);
    }

    public RsaKeyProvider(String id) {
        this(generateRsaKey(id), id, JWSAlgorithm.RS256);
    }

    @Override
    public boolean supports(String clientId) {
        return true;
    }

    @Override
    public RSAKey getKey() {
        return new RSAKey.Builder(rsaKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(algorithm)
                .keyID(id)
                .build();
    }

    @Override
    public String generateToken(JWTClaimsSet claims) {
        try {
            SignedJWT signedJWT = new SignedJWT(header(), claims);
            signedJWT.sign(new RSASSASigner(getKey()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new FakeJwtException(e);
        }
    }

    private JWSHeader header() {
        return new JWSHeader.Builder(algorithm).keyID(id).type(JOSEObjectType.JWT).build();
    }

    private static RSAKey generateRsaKey(String id) {
        try {
            return new RSAKeyGenerator(2048)
                    .keyID(id)
                    .generate();
        } catch (JOSEException e) {
            throw new FakeJwtException(e);
        }
    }

}
