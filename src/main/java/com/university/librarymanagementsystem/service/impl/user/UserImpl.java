package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.user.UserDTO;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
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
        User user = userRepo.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("not exisiting" +
                user_id));

        return UserMapper.mapToUserDTO(user);
    }
}
