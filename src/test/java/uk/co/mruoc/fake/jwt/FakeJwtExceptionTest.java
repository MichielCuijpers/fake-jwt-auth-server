package uk.co.mruoc.fake.jwt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FakeJwtExceptionTest {

    @Test
    void shouldReturnMessage() {
        String message = "my-message";

        Throwable error = new FakeJwtException(message);

        assertThat(error.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldReturnCause() {
        Throwable cause = new Exception("my-cause");

        Throwable error = new FakeJwtException(cause);

        assertThat(error.getCause()).isEqualTo(cause);
    }

}
