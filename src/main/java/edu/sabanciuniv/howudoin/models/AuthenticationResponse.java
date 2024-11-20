package edu.sabanciuniv.howudoin.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponse {
    // Getter
    private String jwt;

    // Constructor
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

}