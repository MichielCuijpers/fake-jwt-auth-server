package uk.mruoc.fake.jwt;

public class FakeJwtClientException extends RuntimeException {

    public FakeJwtClientException(String message) {
        super(message);
    }

    public FakeJwtClientException(Throwable cause) {
        super(cause);
    }

}
