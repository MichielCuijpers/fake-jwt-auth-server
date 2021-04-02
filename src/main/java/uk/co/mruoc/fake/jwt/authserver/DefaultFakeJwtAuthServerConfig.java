package uk.co.mruoc.fake.jwt.authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.Extension;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.fake.jwt.token.TokenGenerator;
import uk.co.mruoc.fake.jwt.token.TokenResponseTransformer;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;

import java.time.Clock;

@RequiredArgsConstructor
public class DefaultFakeJwtAuthServerConfig implements FakeJwtAuthServerConfig {

    private final WireMockConfiguration wireMockConfig;
    private final JsonWebKeySetProvider keySetProvider;
    private final JsonConverter jsonConverter;
    private final Clock clock;

    public DefaultFakeJwtAuthServerConfig() {
        this(new JsonWebKeySetProvider(), Clock.systemUTC());
    }

    public DefaultFakeJwtAuthServerConfig(Clock clock) {
        this(new JsonWebKeySetProvider(), clock);
    }

    public DefaultFakeJwtAuthServerConfig(JsonWebKeySetProvider keySetProvider, Clock clock) {
        this(toWireMockConfig(keySetProvider, clock), keySetProvider, new JacksonJsonConverter(new ObjectMapper()), clock);
    }

    @Override
    public WireMockConfiguration getWiremockConfig() {
        return wireMockConfig;
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
        return toTokenGenerator(keySetProvider, clock);
    }

    private static WireMockConfiguration toWireMockConfig(JsonWebKeySetProvider keySetProvider, Clock clock) {
        return WireMockConfiguration.wireMockConfig()
                .extensions(toExtension(keySetProvider, clock))
                .dynamicPort();
    }

    private static Extension toExtension(JsonWebKeySetProvider keySetProvider, Clock clock) {
        return TokenResponseTransformer.builder()
                .tokenGenerator(toTokenGenerator(keySetProvider, clock))
                .build();
    }

    private static TokenGenerator toTokenGenerator(JsonWebKeySetProvider provider, Clock clock) {
        return TokenGenerator.builder()
                .provider(provider)
                .clock(clock)
                .build();
    }

}
