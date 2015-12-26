package com.github.atdixon.vroom2;

public final class NotKnownException extends RuntimeException {

    private final String key;

    public NotKnownException(String key) {
        this.key = key;
    }

    public NotKnownException(String key, Throwable cause) {
        super(cause);
        this.key = key;
    }

    public String key() {
        return key;
    }

}
