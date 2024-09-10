package com.aibackend.aibackend.controller.jwt;

import com.aibackend.aibackend.model.AppUser;
import com.aibackend.aibackend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            // מחזיר 401 במידה והפרטים לא נכונים
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is disabled");
        } catch (Exception e) {
            // טיפול בשגיאה פנימית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during authentication");
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody JwtRequest userRequest) {
        if (userService.findByUsername(userRequest.getUsername()) != null) {
            return ResponseEntity.status(409).body("User already exists");
        }
        try {
            String encodedPass = passwordEncoder.encode(userRequest.getPassword());
            AppUser user = new AppUser();
            user.setUsername(userRequest.getUsername());
            user.setPassword(encodedPass);
            user.setEmail(userRequest.getEmail());
            userService.saveUser(user);
            UserDetails userDetails = new User(userRequest.getUsername(), encodedPass, new ArrayList<>());
            return ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(userDetails)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while registering the user");
        }
    }



    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
