package uk.co.mruoc.fake.jwt.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Builder
@Data
public class TokenRequest {

    @JsonProperty("client_id")
    private final String clientId;
    private final String audience;

    public Optional<String> getAudience() {
        return Optional.ofNullable(audience);
    }

}
