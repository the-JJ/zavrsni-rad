package com.juricicjuraj.ai.planner.web.rest;

class RestException extends RuntimeException {
    private static final int DEFAULT_CODE = 500;
    private int status;

    RestException(Throwable e, int status) {
        super(e);

        this.status = status;
    }

    RestException(Throwable e) {
        this(e, DEFAULT_CODE);
    }

    RestException(String message, int status) {
        super(message);

        this.status = status;
    }

    RestException(String message) {
        this(message, DEFAULT_CODE);
    }

    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }
}
