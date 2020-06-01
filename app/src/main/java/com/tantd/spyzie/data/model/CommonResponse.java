package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;

public class CommonResponse {

    @Expose
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "success=" + success +
                '}';
    }
}
