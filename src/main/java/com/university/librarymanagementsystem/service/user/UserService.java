package com.university.librarymanagementsystem.service.user;

import com.university.librarymanagementsystem.dto.user.UserDTO;

public interface UserService {
    UserDTO fetchUserById(String user_id);

}
