package uk.co.mruoc.fake.jwt;

public class FakeJwtException extends RuntimeException {

    public FakeJwtException(String message) {
        super(message);
    }

    public FakeJwtException(Throwable cause) {
        super(cause);
    }

}
