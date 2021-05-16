package com.yassinebassii.cinema.web.authentication;

import com.yassinebassii.cinema.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class UserPublicInfo implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public UserPublicInfo(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
