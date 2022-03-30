package com.example.springfileuploader.controller.models;

import java.io.Serial;
import java.io.Serializable;

public class JwtDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -726348264823621L;
    private final String token;

    public JwtDTO(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    @Override
    public String toString() {
        return "JwtDTO{" +
                "token='" + token + '\'' +
                '}';
    }
}
