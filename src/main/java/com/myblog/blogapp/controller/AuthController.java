package com.myblog.blogapp.controller;

import com.myblog.blogapp.entities.Role;
import com.myblog.blogapp.entities.User;
import com.myblog.blogapp.payload.JWTAuthResponse;
import com.myblog.blogapp.payload.LoginDto;

import com.myblog.blogapp.payload.SignUpDto;
import com.myblog.blogapp.repository.RoleRepository;
import com.myblog.blogapp.repository.UserRepository;
import com.myblog.blogapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager AuthenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/sigin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
//        String userName=loginDto.getUsernameOrEmail();


       Authentication authentication = AuthenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token from tokenProvider

        String token= tokenProvider.generateToken(authentication);
      return ResponseEntity.ok(new JWTAuthResponse(token));


    }

    @PostMapping("/signup")
    public ResponseEntity<?>registerUser(@RequestBody SignUpDto signUpDto){
        // add check for username exist in DB
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!",HttpStatus.BAD_REQUEST);
        }
        //add cheek for email exists in Db
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken",HttpStatus.BAD_REQUEST);
        }
// create user object
        User user= new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
       // will set Admin role for user by default
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);


        return new ResponseEntity<>("User registration successfully",HttpStatus.OK);
    }


}
