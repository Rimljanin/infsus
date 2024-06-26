package com.example.infsus.controller;



import com.example.infsus.model.User;
import com.example.infsus.requests.AuthRequest;
import com.example.infsus.requests.AuthResponse;
import com.example.infsus.requests.RegisterRequest;
import com.example.infsus.service.UserService;
import com.example.infsus.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            final String jwt = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt, authRequest.getUsername()));
        } catch (BadCredentialsException e) {
            throw new Exception("Nevažeći podatci za login");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {

        if (userService.existsByUsername(registerRequest.getUserName()) && userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Korisničko ime i email već postoje");
        } else if (userService.existsByUsername(registerRequest.getUserName())) {
            return ResponseEntity.badRequest().body("Korisničko ime već postoji");
        } else if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email već postoji");
        }

        User user = new User(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userService.save(user);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(registerRequest.getUserName());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt, user.getUserName()));
    }

    @GetMapping("/validate-token/public")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            boolean isValid = jwtTokenUtil.isTokenValid(token);
            if (!isValid) {
                throw new AuthenticationException("Token is invalid or expired") {};
            }
            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            throw new AuthenticationException("An internal server error occurred: " + e.getMessage()) {};
        }
    }
}

