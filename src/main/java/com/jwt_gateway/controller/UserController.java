package com.jwt_gateway.controller;


import com.jwt_gateway.entity.*;
import com.jwt_gateway.service.UserDataServ;
import com.jwt_gateway.service.UserService;
import com.jwt_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDataServ userDataServ;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public UserData register(@RequestBody UserData user) throws Exception {

        user.setProfile("default.png");
        //encoding password with bcryptpasswordencoder

        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        Set<UserRole> roles = new HashSet<>();

        Role role = new Role();
        role.setRoleId(45L);
        role.setRoleName("NORMAL");

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        roles.add(userRole);


        return this.userDataServ.createUser(user, roles);

//        return userService.register(user.getUsername(), user.getPassword());
    }

    @PostMapping("/generate-token")
    public ResponseEntity<?> login(@RequestBody UserData use) {
        String token= userService.login(use.getUsername(),use.getPassword().toString());
        if (token != null) {
//            return token;
            return ResponseEntity.ok(new JwtResponse(token));

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Your custom message here");

//            return "Invalid credentials";
        }


//        throw new RuntimeException("Invalid credentials");
    }


    @GetMapping("/{username}")
    public ResponseEntity<?>  getUser(@PathVariable("username") String username) {
        System.out.println(userService.getUser(username) + "::::::::::::::::::::::::");
        if (this.userService.getUser(username)==null) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Is Not Found");
        } else {
            return  ResponseEntity.status(HttpStatus.OK).body(this.userService.getUser(username));


        }
    }
    //delete the user by id
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userService.deleteUser(userId);
    }



}
