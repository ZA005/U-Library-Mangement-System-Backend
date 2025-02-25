package com.university.librarymanagementsystem.mapper.user;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.user.UserDTO;
import com.university.librarymanagementsystem.entity.user.User;

@Component
public class UserMapper {

    public static UserDTO mapToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getRole(),
                user.getSuffix(),
                user.getContactNo(),
                user.getEmailAdd(),
                user.getStatus(),
                user.getDepartment(),
                user.getProgram());
    }

    public static User mapToUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getFirstName(),
                userDTO.getMiddleName(),
                userDTO.getLastName(),
                userDTO.getSuffix(),
                userDTO.getRole(),
                userDTO.getContactNo(),
                userDTO.getEmailAdd(),
                userDTO.getStatus(),
                userDTO.getDepartment(),
                userDTO.getProgram());
    }
}
