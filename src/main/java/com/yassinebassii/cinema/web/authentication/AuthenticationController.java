package com.yassinebassii.cinema.web.authentication;

import com.yassinebassii.cinema.dao.RoleRepository;
import com.yassinebassii.cinema.dao.UserRepository;
import com.yassinebassii.cinema.entities.Role;
import com.yassinebassii.cinema.entities.User;
import com.yassinebassii.cinema.service.JwtUtil;
import com.yassinebassii.cinema.web.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtutil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path = "/auth/signin")
    public ResponseEntity<?> signin(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                new Response(400, "Incorrect username or password", null)
            );
        }
        System.out.println("Authenticated");
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String jwt = jwtutil.generateToken(userDetails);
        User user = userRepository.findUserByEmail(userDetails.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, new UserPublicInfo(user));
        return ResponseEntity.ok().body(new Response(200, "", authenticationResponse));
    }

    @PostMapping(path = "/auth/signup")
    public ResponseEntity signup(@RequestBody SignupUserRequest signupUserRequest){
        User user = userRepository.findUserByEmail(signupUserRequest.getEmail());
        if(user != null){
            return ResponseEntity.badRequest().body(new Response(400, "email already existe", null));
        }
        if(!signupUserRequest.getPassword().equals(signupUserRequest.getPasswordConfirmation())){
            return ResponseEntity.badRequest().body(new Response(400, "password and password confirmation don't match", null));
        }
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByRole("USER"));
        user = new User(
            null,
            signupUserRequest.getFirstName(),
            signupUserRequest.getLastName(),
            signupUserRequest.getEmail(),
            passwordEncoder.encode(signupUserRequest.getPassword()),
            true,
            roles
        );
        userRepository.save(user);
        return ResponseEntity.ok().body(new Response(200, "", new UserPublicInfo(user)));
    }

    @GetMapping(path = "/auth/user")
    public ResponseEntity authenticatedUser(@AuthenticationPrincipal UserDetails userDetails){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getDetails());
        User user = userRepository.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok().body(new Response(200, "", new UserPublicInfo(user)));
    }
}

@Data @AllArgsConstructor @NoArgsConstructor @ToString
class SignupUserRequest{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirmation;
}
