package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {

    @Expose
    private String token;
    @Expose
    @SerializedName("token_type")
    private String tokenType;
    @Expose
    @SerializedName("expires_in")
    private int expired;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expired=" + expired +
                '}';
    }
}
