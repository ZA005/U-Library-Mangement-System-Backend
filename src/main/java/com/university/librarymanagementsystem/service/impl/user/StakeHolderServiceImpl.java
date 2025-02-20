package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.user.StakeholdersDto;
import com.university.librarymanagementsystem.dto.user.UserDetailsDto;
import com.university.librarymanagementsystem.entity.user.StakeHolders;
import com.university.librarymanagementsystem.entity.user.Users;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.user.StakeHolderMapper;
import com.university.librarymanagementsystem.repository.user.StakeHolderRepository;
import com.university.librarymanagementsystem.repository.user.UserRepo;
import com.university.librarymanagementsystem.service.user.StakeHolderService;

@Service
public class StakeHolderServiceImpl implements StakeHolderService {

    @Autowired
    private StakeHolderRepository stakeHolderRepository;

    @Autowired
    private UserRepo userRepo;

    @Override
    public StakeholdersDto getStakeholderById(String id) {

        StakeHolders stakeHolders = stakeHolderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        return StakeHolderMapper.mapToStakeHoldersDto(stakeHolders);
    }

    // SendingEmail
    @Override
    public StakeholdersDto geStakeHolder(String studentId) {
        StakeHolders stakeHolders = stakeHolderRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("UNC ID Number NOT FOUND"));

        return StakeHolderMapper.mapToStakeHoldersDto(stakeHolders);
    }

    @Override
    public UserDetailsDto getUserDetails(String uncIdNumber) {
        StakeHolders stakeHolders = stakeHolderRepository.findById(uncIdNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + uncIdNumber));
        Users user = userRepo.findBySchoolId(uncIdNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + uncIdNumber));

        UserDetailsDto userDetailsDto = StakeHolderMapper.mapToUserDetailsDto(stakeHolders);

        userDetailsDto.setRole(user.getRole());

        return userDetailsDto;
    }

}
