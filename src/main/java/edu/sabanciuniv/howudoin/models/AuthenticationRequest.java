package edu.sabanciuniv.howudoin.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    // Getters and Setters
    private String email;
    private String password;

    // Default constructor
    public AuthenticationRequest() {}

}