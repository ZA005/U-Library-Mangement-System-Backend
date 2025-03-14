package com.university.librarymanagementsystem.mapper.curriculum;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.curriculum.ProgramDTO;
import com.university.librarymanagementsystem.entity.curriculum.Department;
import com.university.librarymanagementsystem.entity.curriculum.Program;

@Component
public class ProgramMapper {

    public static ProgramDTO mapToProgramDTO(Program program) {
        return new ProgramDTO(
                program.getId(),
                program.getDepartment().getId(),
                program.getDepartment().getName(),
                program.getDepartment().getCode(),
                program.getCode(),
                program.getDescription(),
                program.getStatus());
    }

    public static Program mapToProgram(ProgramDTO programDTO) {
        Department dept = new Department();
        dept.setId(programDTO.getDepartment_id());
        return new Program(
                programDTO.getId(),
                dept,
                programDTO.getCode(),
                programDTO.getDescription(),
                programDTO.getStatus());

    }
}