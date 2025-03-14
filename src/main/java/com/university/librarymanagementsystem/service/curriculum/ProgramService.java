package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.curriculum.ProgramDTO;

public interface ProgramService {
    ProgramDTO addProgram(ProgramDTO programDTO);

    List<ProgramDTO> uploadPrograms(List<ProgramDTO> programDTOs);

    List<ProgramDTO> getAllPrograms();

    List<ProgramDTO> getAllProgramsByDepartment(String departmentID);
}