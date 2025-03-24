package com.university.librarymanagementsystem.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT email_address FROM users WHERE id = :id", nativeQuery = true)
    String fetchEmailById(@Param("id") String id);
}
