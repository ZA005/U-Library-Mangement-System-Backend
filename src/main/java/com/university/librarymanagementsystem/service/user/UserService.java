package com.university.librarymanagementsystem.service.user;

import java.util.List;

import com.university.librarymanagementsystem.dto.user.UserDTO;

public interface UserService {
    UserDTO fetchUserById(String user_id);

    List<UserDTO> uploadUsers(List<UserDTO> userDTO);
}
