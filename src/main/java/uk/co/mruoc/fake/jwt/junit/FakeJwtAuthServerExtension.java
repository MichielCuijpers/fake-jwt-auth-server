package uk.co.mruoc.fake.jwt.junit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import uk.co.mruoc.fake.jwt.authserver.FakeJwtAuthServer;
import uk.co.mruoc.fake.jwt.authserver.FakeJwtAuthServerConfig;
import uk.co.mruoc.fake.jwt.token.Token;
import uk.co.mruoc.fake.jwt.token.TokenRequest;

@RequiredArgsConstructor
public class FakeJwtAuthServerExtension implements BeforeAllCallback, AfterAllCallback {

    private final FakeJwtAuthServer server;

    public FakeJwtAuthServerExtension(FakeJwtAuthServerConfig config) {
        this(new FakeJwtAuthServer(config));
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        server.start();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        server.stop();
    }

    public Token generateToken(TokenRequest request) {
        return server.generateToken(request);
    }

    public String localUri() {
        return server.localUri();
    }

}
