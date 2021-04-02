package uk.co.mruoc.fake.jwt;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.fake.jwt.authserver.DefaultFakeJwtAuthServerConfig;
import uk.co.mruoc.fake.jwt.authserver.FakeJwtAuthServer;

@Slf4j
public class Main {

    private static final FakeJwtAuthServer SERVER = new FakeJwtAuthServer(new DefaultFakeJwtAuthServerConfig());

    public static void main(String[] args) {
        log.info("server running at {}", SERVER.start());
        Runtime.getRuntime().addShutdownHook(new Thread(SERVER::stop));
    }

    public static String getUri() {
        return SERVER.localUri();
    }

}
