package com.university.librarymanagementsystem.service.user;

import com.university.librarymanagementsystem.dto.user.StakeholdersDto;
import com.university.librarymanagementsystem.dto.user.UserDetailsDto;

public interface StakeHolderService {

    StakeholdersDto getStakeholderById(String id);

    StakeholdersDto geStakeHolder(String studentId);

    UserDetailsDto getUserDetails(String uncIdNumber);
}
