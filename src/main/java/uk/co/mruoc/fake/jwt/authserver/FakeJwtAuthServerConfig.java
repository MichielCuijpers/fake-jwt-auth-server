package uk.co.mruoc.fake.jwt.authserver;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import uk.co.mruoc.fake.jwt.token.TokenGenerator;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;
import uk.co.mruoc.json.JsonConverter;

import java.time.Clock;

public interface FakeJwtAuthServerConfig {

    int getPort();

    Clock getClock();

    String getIssuer();

    String getDefaultAudience();

    WireMockConfiguration getWiremockConfig();

    JsonWebKeySetProvider getKeySetProvider();

    JsonConverter getJsonConverter();

    TokenGenerator getTokenGenerator();

}
