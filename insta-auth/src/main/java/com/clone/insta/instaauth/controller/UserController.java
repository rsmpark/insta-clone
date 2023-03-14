package com.clone.insta.instaauth.controller;

import com.clone.insta.instaauth.exception.BadRequestException;
import com.clone.insta.instaauth.exception.EmailAlreadyExistsException;
import com.clone.insta.instaauth.exception.ResourceNotFoundException;
import com.clone.insta.instaauth.exception.UsernameAlreadyExistsException;
import com.clone.insta.instaauth.model.InstaUserDetails;
import com.clone.insta.instaauth.model.Profile;
import com.clone.insta.instaauth.model.User;
import com.clone.insta.instaauth.payload.request.LoginRequest;
import com.clone.insta.instaauth.payload.request.SingupRequest;
import com.clone.insta.instaauth.payload.response.ApiResponse;
import com.clone.insta.instaauth.payload.response.JwtAuthResponse;
import com.clone.insta.instaauth.payload.response.UserResponse;
import com.clone.insta.instaauth.service.JwtTokenProvider;
import com.clone.insta.instaauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Creating user {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SingupRequest signupRequest) {
        log.info("Creating user {}", signupRequest.getUsername());

        User user = User.builder().username(signupRequest.getUsername())
                .email(signupRequest.getEmail()).password(signupRequest.getPassword())
                .userProfile(Profile.builder().displayName(signupRequest.getName()).build())
                .build();

        try {
            userService.registerUser(user);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{username}").buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

    @PutMapping("/users/me/picture")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateProfilePicture(@RequestBody String profilePicture, @AuthenticationPrincipal InstaUserDetails userDetails) {
        log.info("Update user {} picture", userDetails.getUsername());

        userService.updateProfilePicture(profilePicture, userDetails.getId());

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Profile picture updated successfully"));
    }

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        log.info("Retrieving user {}", username);

        return userService.findByUsername(username).map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("Retrieving all users");

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails) {
        return UserResponse.builder().id(userDetails.getId()).username(userDetails.getUsername())
                .name(userDetails.getUserProfile().getDisplayName())
                .profilePicture(userDetails.getUserProfile().getProfilePictureUrl()).build();
    }

    @GetMapping(value = "/users/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserResponse(@PathVariable("username") String username) {
        log.info("Retrieving user {}", username);

        return userService.findByUsername(username).map(user -> ResponseEntity.ok(convertTo(user)))
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @PostMapping(value = "/users/summary/in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummaries(@RequestBody List<String> usernames) {
        log.info("Retrieving summaries for {} usernames", usernames.size());

        List<UserResponse> summaries = userService.findByUsernameIn(usernames).stream()
                .map(user -> convertTo(user)).collect(Collectors.toList());

        return ResponseEntity.ok(summaries);

    }

    private UserResponse convertTo(User user) {
        return UserResponse.builder().id(user.getId()).username(user.getUsername())
                .name(user.getUserProfile().getDisplayName())
                .profilePicture(user.getUserProfile().getProfilePictureUrl()).build();
    }
}