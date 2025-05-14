package com.university.librarymanagementsystem.service.impl.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.user.UserDTO;
import com.university.librarymanagementsystem.mapper.user.UserMapper;
import com.university.librarymanagementsystem.repository.user.UserRepository;
import com.university.librarymanagementsystem.service.user.UserService;
import com.university.librarymanagementsystem.entity.user.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserImpl implements UserService {

    private UserRepository userRepo;

    @Override
    public UserDTO fetchUserById(String user_id) {
        User user = userRepo.findById(user_id).orElse(null);
        if (user == null) {
            return null;
        }

        return UserMapper.mapToUserDTO(user);
    }

    @Override
    public List<UserDTO> uploadUsers(List<UserDTO> userDTOs) {
        List<User> users = userDTOs.stream()
                .map(UserMapper::mapToUser)
                .collect(Collectors.toList());

        List<User> usersToUpdate = new ArrayList<>();
        List<User> usersToSave = new ArrayList<>();

        for (User user : users) {
            System.out.println("Processing User - ID: " + user.getId() + ", Name: " + user.getFirstName() + " "
                    + user.getLastName());
            User existingUser = userRepo.findById(user.getId()).orElse(null);

            if (existingUser != null) {
                boolean needsUpdate = false;

                if (!equalsNullable(existingUser.getRole(), user.getRole()) ||
                        !equalsNullable(existingUser.getContactNo(), user.getContactNo()) ||
                        !equalsNullable(existingUser.getStatus(), user.getStatus()) ||
                        !equalsNullable(existingUser.getDepartment(), user.getDepartment()) ||
                        !equalsNullable(existingUser.getProgram(), user.getProgram())) {
                    needsUpdate = true;
                }

                if (needsUpdate) {
                    usersToUpdate.add(user);
                }
            } else {
                usersToSave.add(user);
            }
        }

        List<User> savedUsers = new ArrayList<>();
        if (!usersToSave.isEmpty()) {
            savedUsers = userRepo.saveAll(usersToSave);
        }

        if (!usersToUpdate.isEmpty()) {
            userRepo.saveAll(usersToUpdate);
        }

        List<User> finalUsers = new ArrayList<>();
        finalUsers.addAll(savedUsers);
        finalUsers.addAll(usersToUpdate);

        return finalUsers.stream()
                .map(UserMapper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    // Utility method to safely compare nullable objects
    private boolean equalsNullable(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
