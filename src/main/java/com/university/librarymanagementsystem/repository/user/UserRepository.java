package com.university.librarymanagementsystem.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
