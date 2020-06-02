package com.tantd.spyzie.core;

/**
 * Created by tantd on 8/22/2017.
 */
public class DataLayerException extends Exception {
    
    private final int errorCode;
    private final String errorMsg;

    public DataLayerException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
