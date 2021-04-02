package uk.co.mruoc.fake.jwt.authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.Extension;
import lombok.Builder;
import lombok.Data;
import uk.co.mruoc.fake.jwt.token.TokenGenerator;
import uk.co.mruoc.fake.jwt.token.TokenResponseTransformer;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;

import java.time.Clock;

@Builder
@Data
public class DefaultFakeJwtAuthServerConfig implements FakeJwtAuthServerConfig {

    private static final int DYNAMIC_PORT = 0;

    @Builder.Default
    private final int port = DYNAMIC_PORT;

    @Builder.Default
    private final JsonWebKeySetProvider keySetProvider = new JsonWebKeySetProvider();

    @Builder.Default
    private final JsonConverter jsonConverter = new JacksonJsonConverter(new ObjectMapper());

    @Builder.Default
    private final Clock clock = Clock.systemUTC();

    @Builder.Default
    private final String defaultAudience = "default-audience";

    @Builder.Default
    private final String issuer = "default-issuer";

    @Override
    public WireMockConfiguration getWiremockConfig() {
        return toWireMockConfig();
    }

    @Override
    public JsonWebKeySetProvider getKeySetProvider() {
        return keySetProvider;
    }

    @Override
    public JsonConverter getJsonConverter() {
        return jsonConverter;
    }

    @Override
    public TokenGenerator getTokenGenerator() {
        return toTokenGenerator();
    }

    private WireMockConfiguration toWireMockConfig() {
        return WireMockConfiguration.wireMockConfig()
                .extensions(toExtension())
                .port(port);
    }

    private Extension toExtension() {
        return TokenResponseTransformer.builder()
                .tokenGenerator(toTokenGenerator())
                .build();
    }

    private TokenGenerator toTokenGenerator() {
        return TokenGenerator.builder()
                .issuer(issuer)
                .defaultAudience(defaultAudience)
                .keySetProvider(keySetProvider)
                .clock(clock)
                .build();
    }

}
