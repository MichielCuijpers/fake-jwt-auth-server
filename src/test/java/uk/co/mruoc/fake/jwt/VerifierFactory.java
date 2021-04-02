package uk.co.mruoc.fake.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import uk.mruoc.fake.jwt.FakeJwtClient;

public class VerifierFactory {

    public static JWSVerifier getRsaJwsVerifierFromKeySet(FakeJwtClient client) throws JOSEException {
        JWKSet jwkSet = client.getKeySet();
        RSAKey rsaKey = jwkSet.getKeyByKeyId("default-rsa-key-id").toRSAKey();
        return new RSASSAVerifier(rsaKey);
    }

}
