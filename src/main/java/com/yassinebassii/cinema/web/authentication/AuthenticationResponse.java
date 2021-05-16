package com.yassinebassii.cinema.web.authentication;

import com.yassinebassii.cinema.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class AuthenticationResponse implements Serializable {
    private String token;
    private UserPublicInfo user;
}
