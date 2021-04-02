package uk.co.mruoc.fake.jwt.key;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;

public interface JsonWebKeyProvider {

    boolean supports(String clientId);

    JWK getKey();

    String generateToken(JWTClaimsSet claims);

}
