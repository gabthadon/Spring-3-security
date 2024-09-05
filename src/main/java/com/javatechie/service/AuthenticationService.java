package com.javatechie.service;

import com.javatechie.dto.AuthenticationRequest;
import com.javatechie.dto.AuthenticationResponse;
import com.javatechie.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthenticationResponse login(AuthenticationRequest request) throws Exception {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (
                AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        // Load the user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Generate the JWT token
        final String token = jwtUtil.generateToken(userDetails.getUsername());

        // Return the response with the token, username, and password
        return new AuthenticationResponse(token, userDetails.getUsername(), userDetails.getPassword());
    }
}

