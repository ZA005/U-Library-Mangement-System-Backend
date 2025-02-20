package com.university.librarymanagementsystem.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.user.UserDTO;
import com.university.librarymanagementsystem.service.user.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private UserService service;

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable("id") String user_id) {
        return ResponseEntity.ok(service.fetchUserById(user_id));
    }
}
