package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.curriculum.ProgramDTO;
import com.university.librarymanagementsystem.entity.curriculum.Program;

public interface ProgramService {
    ProgramDTO addProgram(ProgramDTO programDTO);

    List<ProgramDTO> uploadPrograms(List<ProgramDTO> programDTOs);

    // void updateMultiplePrograms(List<Program> programs);
    // ProgramDTO getProgramByID(Integer programID);

    List<ProgramDTO> getAllPrograms();

    List<ProgramDTO> getAllProgramsByDepartment(String departmentID);

    // ProgramDTO updateDepartment(Integer programID, ProgramDTO updatedProgram);
}