package uk.co.mruoc.fake.jwt.authserver;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFakeJwtAuthServerConfigTest {

    @Test
    void shouldReturnZeroAsDynamicPortByDefault() {
        FakeJwtAuthServerConfig config = DefaultFakeJwtAuthServerConfig.builder().build();

        assertThat(config.getPort()).isZero();
    }

    @Test
    void shouldReturnJsonWebKeySetProviderByDefault() {
        FakeJwtAuthServerConfig config = DefaultFakeJwtAuthServerConfig.builder().build();

        assertThat(config.getKeySetProvider()).isInstanceOf(JsonWebKeySetProvider.class);
    }

    @Test
    void shouldReturnJacksonJsonConverterByDefault() {
        FakeJwtAuthServerConfig config = DefaultFakeJwtAuthServerConfig.builder().build();

        assertThat(config.getJsonConverter()).isInstanceOf(JacksonJsonConverter.class);
    }

    @Test
    void shouldReturnSystemUtcClockByDefault() {
        FakeJwtAuthServerConfig config = DefaultFakeJwtAuthServerConfig.builder().build();

        assertThat(config.getClock()).isEqualTo(Clock.systemUTC());
    }

    @Test
    void shouldReturnDefaultIssuerByDefault() {
        FakeJwtAuthServerConfig config = DefaultFakeJwtAuthServerConfig.builder().build();

        assertThat(config.getIssuer()).isEqualTo("default-issuer");
    }

    @Test
    void shouldReturnDefaultAudienceByDefault() {
        FakeJwtAuthServerConfig config = DefaultFakeJwtAuthServerConfig.builder().build();

        assertThat(config.getDefaultAudience()).isEqualTo("default-audience");
    }

}
