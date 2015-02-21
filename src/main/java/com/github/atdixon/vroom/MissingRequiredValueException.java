package com.github.atdixon.vroom;

public final class MissingRequiredValueException extends RuntimeException {

    public MissingRequiredValueException(String attribute) {
        super("attribute: " + attribute);
    }

}
