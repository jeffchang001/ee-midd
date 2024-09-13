package com.sogo.ee.midd.exception;

public class RadarAPIException extends Exception {
    public RadarAPIException(String message) {
        super(message);
    }

    public RadarAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
