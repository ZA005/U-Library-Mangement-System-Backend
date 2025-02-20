package com.university.librarymanagementsystem.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.user.StakeHolders;
import java.util.List;

@Repository
public interface StakeHolderRepository extends JpaRepository<StakeHolders, String> {

}
