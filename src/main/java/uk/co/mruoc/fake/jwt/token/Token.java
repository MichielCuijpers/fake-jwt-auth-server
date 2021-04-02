package uk.co.mruoc.fake.jwt.token;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Builder
@Data
public class Token {

    private final String value;
    private final Duration expiry;

    public long getExpiryMillis() {
        return expiry.toMillis();
    }

}
