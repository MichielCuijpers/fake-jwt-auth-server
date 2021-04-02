package uk.co.mruoc.fake.jwt;

import org.junit.jupiter.api.Test;
import uk.mruoc.fake.jwt.FakeJwtClient;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    @Test
    void shouldStartServer() {
        Main.main(new String[0]);
        FakeJwtClient client = new FakeJwtClient(Main.getUri());

        assertThat(client.getKeySet()).isNotNull();
    }

}
