package com.university.librarymanagementsystem.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.LoginDto;
import com.university.librarymanagementsystem.dto.UsersDto;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.service.UsersService;

import lombok.AllArgsConstructor;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private UsersService usersService;

    // Build Add User Rest API
    @PostMapping
    public ResponseEntity<UsersDto> createUser(@RequestBody UsersDto usersDto) {
        UsersDto savedUser = usersService.createUsers(usersDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Build Get User Rest API
    @GetMapping("{id}")
    public ResponseEntity<UsersDto> getUser(@PathVariable("id") Long userId) {
        UsersDto userDto = usersService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    // Build Check Library Card Number existence REST API
    @GetMapping("/check-library-card")
    public ResponseEntity<Boolean> checkLibraryCard(@RequestParam("libCardNum") String libraryCardNumber) {
        boolean exists = usersService.doesLibraryCardNumberExist(libraryCardNumber);
        return ResponseEntity.ok(exists);
    }

    // Build Get All Users REST API
    @GetMapping
    public ResponseEntity<List<UsersDto>> getAllUsers() {
        List<UsersDto> users = usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Build Update User REST API
    @PutMapping("{id}")
    public ResponseEntity<UsersDto> updateUser(@PathVariable("id") Long userId, @RequestBody UsersDto updatedUser) {
        UsersDto userDto = usersService.updateUser(userId, updatedUser);

        return ResponseEntity.ok(userDto);
    }

    // Build Delete User REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        usersService.deleteUser(userId);

        return ResponseEntity.ok("User Deleted Succesfuly");
    }

    @PostMapping("/login")
    public ResponseEntity<UsersDto> loginUser(@RequestBody LoginDto loginDto) {

        try {
            UsersDto authenticatedUser = usersService.login(loginDto.getLibraryCardNumber(), loginDto.getPassword());
            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // Invalid credentials
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // User not found
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UsersDto> registerUser(@RequestBody UsersDto usersDto) {
        boolean exists = usersService.doesLibraryCardNumberExist(usersDto.getLibraryCardNumber());
        if (exists) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // If library card number already exists
        }
        UsersDto registeredUser = usersService.createUsers(usersDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
