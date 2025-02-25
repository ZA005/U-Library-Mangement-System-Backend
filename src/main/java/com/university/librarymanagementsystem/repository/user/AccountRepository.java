package com.university.librarymanagementsystem.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.user.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query(value = "SELECT * FROM accounts WHERE user_id = :user_id", nativeQuery = true)
    Optional<Account> findByUserID(@Param("user_id") String user_id);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM accounts WHERE user_id = :user_id)", nativeQuery = true)
    Boolean existByUserID(@Param("user_id") String user_id);

}
