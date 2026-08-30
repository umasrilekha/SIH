package com.govmesh.food.soap.exception;

public class SoapServiceException extends RuntimeException {

    private final String faultCode;

    public SoapServiceException(String faultCode, String message) {
        super(message);
        this.faultCode = faultCode;
    }

    public SoapServiceException(String faultCode, String message, Throwable cause) {
        super(message, cause);
        this.faultCode = faultCode;
    }

    public String getFaultCode() {
        return faultCode;
    }
}
