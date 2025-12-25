package com.project.carsewa.controller;

import com.project.carsewa.dto.AuthenticationRequest;
import com.project.carsewa.dto.AuthenticationResponse;
import com.project.carsewa.dto.SignupRequestDto;
import com.project.carsewa.dto.Userdto;
import com.project.carsewa.entity.User;
import com.project.carsewa.repository.UserRepository;
import com.project.carsewa.service.auth.AuthService;
import com.project.carsewa.service.jwt.UserService;
import com.project.carsewa.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

   private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequestDto signupRequestDto){

        if(authService.hasUserByEmail(signupRequestDto.getEmail()))
        {
            return new ResponseEntity<>("Customer already present", HttpStatus.NOT_ACCEPTABLE);
        }
        Userdto createdCustomer = authService.createCustomer(signupRequestDto);
        if(createdCustomer==null)
        {
            return new ResponseEntity<>("customer not created please try again later", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, DisabledException, UsernameNotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect Username or Password");
        }
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId((optionalUser.get().getId()));
            authenticationResponse.setUserRole(optionalUser.get().getRole());
        }

        return authenticationResponse;
    }
}
