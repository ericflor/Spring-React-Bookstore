package com.example.SpringReactBookstore.controllers;

import com.example.SpringReactBookstore.config.JwtUtil;
import com.example.SpringReactBookstore.models.AuthenticationRequest;
import com.example.SpringReactBookstore.models.AuthenticationResponse;
import com.example.SpringReactBookstore.services.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;

    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (BadCredentialsException exception){
            throw new RuntimeException("Email or Password is incorrect");
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse("Beer " + token));
    }
}