package com.university.librarymanagementsystem.mapper.user;

import com.university.librarymanagementsystem.dto.user.UsersDto;
import com.university.librarymanagementsystem.entity.user.Users;

public class UsersMapper {

    public static UsersDto mapToUsersDto(Users users) {
        return new UsersDto(
                users.getUserId(),
                users.getPassword(),
                users.getSchoolId(),
                users.getRole());
    }

    public static Users mapToUsersDto(UsersDto usersDto) {
        return new Users(
                usersDto.getUserId(),
                usersDto.getPassword(),
                usersDto.getSchoolId(),
                usersDto.getRole(),
                null);
    }

}
