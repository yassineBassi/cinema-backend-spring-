package com.yassinebassii.cinema.web.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class AuthenticationRequest {
    private String email;
    private String password;
}
