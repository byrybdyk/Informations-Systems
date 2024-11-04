package com.byrybdyk.lb1.controller;

import com.byrybdyk.lb1.model.User;
import com.byrybdyk.lb1.model.enums.Role;
import com.byrybdyk.lb1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        if (userService.findByUsername(username).isPresent()) {
            return new ResponseEntity<>("Username is already taken.", HttpStatus.BAD_REQUEST);
        }

        userService.registerNewUser(username, password, Role.USER);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return new ResponseEntity<>("Login successful.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
    }
}
