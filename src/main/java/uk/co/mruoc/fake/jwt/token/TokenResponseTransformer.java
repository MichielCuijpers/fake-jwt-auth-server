package uk.co.mruoc.fake.jwt.token;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.Builder;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import uk.co.mruoc.fake.jwt.FakeJwtException;

@Builder
public class TokenResponseTransformer extends ResponseTransformer {

    @Builder.Default
    private final JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);

    private final TokenGenerator tokenGenerator;

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public String getName() {
        return "token-response-transformer";
    }

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        TokenRequest tokenRequest = toTokenRequest(request);
        return toTokenResponse(response, tokenRequest);
    }

    private Response toTokenResponse(Response response, TokenRequest tokenRequest) {
        Token token = tokenGenerator.generate(tokenRequest);
        return insertToken(token, response);
    }

    private Response insertToken(Token token, Response response) {
        String body = response.getBodyAsString()
                .replace("%TOKEN%", token.getValue())
                .replace("\"%EXPIRY%\"", Long.toString(token.getExpiryMillis()));
        return Response.Builder.like(response).but()
                .body(body)
                .build();
    }

    private TokenRequest toTokenRequest(Request request) {
        JSONObject body = extractBody(request);
        return TokenRequest.builder()
                .clientId(body.getAsString("client_id"))
                .audience(body.getAsString("audience"))
                .build();
    }

    private JSONObject extractBody(Request request) {
        try {
            return parser.parse(request.getBody(), JSONObject.class);
        } catch (ParseException e) {
            throw new FakeJwtException(e);
        }
    }

}
