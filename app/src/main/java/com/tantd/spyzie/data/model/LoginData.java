package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;

import okhttp3.Response;

public class LoginData {

    public static class Request {

        @Expose
        private String email;
        @Expose
        private String password;

        public Request(String email, String password) {
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

    public static class Response {

        @Expose
        private String token;
        @Expose
        private String status;
        @Expose
        private String error;
        @Expose
        private String msg;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "token='" + token + '\'' +
                    ", status='" + status + '\'' +
                    ", error='" + error + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
