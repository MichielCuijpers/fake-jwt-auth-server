package uk.mruoc.fake.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.fake.jwt.token.TokenRequest;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.co.mruoc.rest.client.RestClient;
import uk.co.mruoc.rest.client.SimpleRestClient;
import uk.co.mruoc.rest.client.response.Response;

import java.text.ParseException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@RequiredArgsConstructor
public class FakeJwtClient {

    private final String baseUri;
    private final RestClient client;
    private final JsonConverter jsonConverter;

    public FakeJwtClient(String baseUri) {
        this(baseUri, new SimpleRestClient(), new JacksonJsonConverter(defaultObjectMapper()));
    }

    public JWKSet getKeySet() {
        try {
            return tryGetKeySet();
        } catch (ParseException e) {
            throw new FakeJwtClientException(e);
        }
    }

    public JWKSet tryGetKeySet() throws ParseException {
        Response response = client.get(buildGetJwkSetUri());
        if (!response.is2xx()) {
            throw new FakeJwtClientException(response.getBody());
        }
        return JWKSet.parse(response.getBody());
    }

    public SignedJWT getToken(TokenRequest request) {
        String requestBody = jsonConverter.toJson(request);
        Response response = client.post(buildPostTokenUri(), requestBody);
        return extractToken(response);
    }

    private SignedJWT extractToken(Response response) {
        try {
            return tryExtractToken(response);
        } catch (ParseException e) {
            throw new FakeJwtClientException(e);
        }
    }

    private SignedJWT tryExtractToken(Response response) throws ParseException {
        if (!response.is2xx()) {
            throw new FakeJwtClientException(response.getBody());
        }
        JsonNode node = jsonConverter.toObject(response.getBody(), JsonNode.class);
        return SignedJWT.parse(node.get("access_token").asText());
    }

    private String buildGetJwkSetUri() {
        return String.format("%s/.well-known/jwks.json", baseUri);
    }

    private String buildPostTokenUri() {
        return String.format("%s/oauth/token", baseUri);
    }

    private static ObjectMapper defaultObjectMapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module().configureAbsentsAsNulls(true))
                .setSerializationInclusion(Include.NON_NULL);
    }
}
