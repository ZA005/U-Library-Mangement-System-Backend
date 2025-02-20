package com.university.librarymanagementsystem.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.user.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findBySchoolId(String schoolId);

    Boolean existsBySchoolId(String schoolId);

}
