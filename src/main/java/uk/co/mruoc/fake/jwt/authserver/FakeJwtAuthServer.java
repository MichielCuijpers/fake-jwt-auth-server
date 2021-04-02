package uk.co.mruoc.fake.jwt.authserver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.fake.jwt.token.Token;
import uk.co.mruoc.fake.jwt.token.TokenGenerator;
import uk.co.mruoc.fake.jwt.key.JsonWebKeySetProvider;
import uk.co.mruoc.fake.jwt.token.TokenRequest;
import uk.co.mruoc.file.content.ContentLoader;
import uk.co.mruoc.json.JsonConverter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@RequiredArgsConstructor
public class FakeJwtAuthServer {

    private final JsonWebKeySetProvider keySetProvider;
    private final WireMockServer server;
    private final JsonConverter jsonConverter;
    private final TokenGenerator tokenGenerator;

    public FakeJwtAuthServer(FakeJwtAuthServerConfig config) {
        this(config.getKeySetProvider(),
                toServer(config.getWiremockConfig()),
                config.getJsonConverter(),
                config.getTokenGenerator());
    }

    public String start() {
        server.start();
        setUpJWKSEndpoint();
        setUpJWTEndpoint();
        return localUri();
    }

    public String localUri() {
        return String.format("http://localhost:%d", server.port());
    }

    public Token generateToken(TokenRequest request) {
        return tokenGenerator.generate(request);
    }

    public void stop() {
        server.stop();
    }

    private void setUpJWKSEndpoint() {
        String body = jsonConverter.toJson(keySetProvider.getKeySet().toJSONObject());
        server.stubFor(get(urlEqualTo("/.well-known/jwks.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));
    }

    private void setUpJWTEndpoint() {
        server.stubFor(post(urlEqualTo("/oauth/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withTransformers("token-response-transformer")
                        .withBody(ContentLoader.loadContentFromClasspath("response-template.json"))));
    }

    private static WireMockServer toServer(WireMockConfiguration config) {
        return new WireMockServer(config);
    }

}
