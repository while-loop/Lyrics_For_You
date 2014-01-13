package com.lyics4me.lyrics4u;

class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
    private String err;

    public CustomException() {
        super();
        err = "null";
    }

    public CustomException(String message) {
        super(message);
        err = message;
    }

    public String toString() {
        return err;
    }
}
