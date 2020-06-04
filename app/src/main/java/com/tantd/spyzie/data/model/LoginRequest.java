package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;

public class LoginRequest {

    @Expose
    private String email;
    @Expose
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
