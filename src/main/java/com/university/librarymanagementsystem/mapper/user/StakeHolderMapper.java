package com.university.librarymanagementsystem.mapper.user;

import com.university.librarymanagementsystem.dto.circulation.BorrowerDetailsDto;
import com.university.librarymanagementsystem.dto.user.StakeholdersDto;
import com.university.librarymanagementsystem.dto.user.UserDetailsDto;
import com.university.librarymanagementsystem.entity.user.StakeHolders;

public class StakeHolderMapper {

    public static StakeholdersDto mapToStakeHoldersDto(StakeHolders stakeholders) {
        return new StakeholdersDto(
                stakeholders.getId(),
                stakeholders.getFirstName(),
                stakeholders.getMiddleName(),
                stakeholders.getLastName(),
                stakeholders.getSuffix(),
                stakeholders.getDepartment(),
                stakeholders.getProgram(),
                stakeholders.getContactNum(),
                stakeholders.getEmailAdd(),
                stakeholders.getStatus());
    }

    public static StakeHolders mapToStakeHolderDto(StakeholdersDto stakeholdersDto) {
        return new StakeHolders(
                stakeholdersDto.getId(),
                stakeholdersDto.getFirstName(),
                stakeholdersDto.getMiddleName(),
                stakeholdersDto.getLastName(),
                stakeholdersDto.getSuffix(),
                stakeholdersDto.getDepartment(),
                stakeholdersDto.getProgram(),
                stakeholdersDto.getContactNum(),
                stakeholdersDto.getEmailAdd(),
                stakeholdersDto.getStatus());
    }

    public static BorrowerDetailsDto mapToBorrowerDetailsDto(StakeHolders stakeHolders) {
        BorrowerDetailsDto borrower = new BorrowerDetailsDto();
        borrower.setIdNumber(stakeHolders.getId());
        borrower.setDepartment(stakeHolders.getDepartment().getName());

        return borrower;
    }

    public static UserDetailsDto mapToUserDetailsDto(StakeHolders stakeholder) {
        UserDetailsDto userDetails = new UserDetailsDto();
        userDetails.setId(stakeholder.getId());
        userDetails.setFullName(fullName(stakeholder.getFirstName(), stakeholder.getMiddleName(),
                stakeholder.getLastName(), stakeholder.getSuffix()));
        userDetails.setDepartmentId(stakeholder.getDepartment().getId());
        userDetails.setDepartmentName(stakeholder.getDepartment().getName());
        userDetails.setProgramId(stakeholder.getProgram().getProgram_id());
        userDetails.setProgramName(stakeholder.getProgram().getDescription());
        userDetails.setEmailAdd(stakeholder.getEmailAdd());
        userDetails.setContactNum(stakeholder.getContactNum());
        return userDetails;

    }

    private static String fullName(String firstName, String middleName, String lastName, String suffix) {
        return String.format("%s %s %s %s",
                firstName,
                middleName != null && !middleName.isEmpty() ? middleName : "",
                lastName,
                suffix != null && !suffix.isEmpty() ? suffix : "").trim();
    }

}
