package com.university.librarymanagementsystem.service.impl.user;

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
}
