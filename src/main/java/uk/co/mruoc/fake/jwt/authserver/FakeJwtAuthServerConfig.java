package uk.co.mruoc.fake.jwt.authserver;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import uk.co.mruoc.fake.jwt.token.TokenGenerator;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;
import uk.co.mruoc.json.JsonConverter;

public interface FakeJwtAuthServerConfig {

    WireMockConfiguration getWiremockConfig();

    JsonWebKeySetProvider getKeySetProvider();

    JsonConverter getJsonConverter();

    TokenGenerator getTokenGenerator();

}
