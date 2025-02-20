package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.curriculum.ProgramDTO;
import com.university.librarymanagementsystem.entity.curriculum.Department;
import com.university.librarymanagementsystem.entity.curriculum.Program;
import com.university.librarymanagementsystem.exception.DuplicateEntryException;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.curriculum.ProgramMapper;
import com.university.librarymanagementsystem.repository.curriculum.DepartmentRepository;
import com.university.librarymanagementsystem.repository.curriculum.ProgramRepository;
import com.university.librarymanagementsystem.service.curriculum.ProgramService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProgramServiceImpl implements ProgramService {
    private ProgramRepository programRepository;
    private DepartmentRepository departmentRepository;

    @Override
    public ProgramDTO addProgram(ProgramDTO programDTO) {
        System.out.println("DEPARTMENT ID:" + programDTO.getDepartment_id());
        Department department = departmentRepository.findById(programDTO.getDepartment_id())
                .orElseThrow(() -> new RuntimeException("Department not Found!"));

        Program program = ProgramMapper.mapToProgram(programDTO);

        program.setDepartment(department);

        Program savedProgram = programRepository.save(program);

        return ProgramMapper.mapToProgramDTO(savedProgram);
    }

    @Override
    public List<ProgramDTO> uploadPrograms(List<ProgramDTO> programDTOs) {

        // SET DEFAULT VALUE OF STATUS TO 1
        // programDTOs.forEach(dto -> {
        // dto.setStatus((byte) 1);
        // });
        programDTOs.forEach(dto -> {
            System.out.println(dto.getProgram_id());
            System.out.println(dto.getDepartment_id());
            System.out.println(dto.getDescription());
            System.out.println(dto.getStatus());
            System.out.println(dto.getCode());
            // System.out.println(dto.getProgram_id());

        });
        List<Program> programs = programDTOs.stream()
                .map(programDTO -> {
                    Department department = departmentRepository.findById(programDTO.getDepartment_id())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Department with ID " + programDTO.getDepartment_id() + " not found!"));
                    Program program = ProgramMapper.mapToProgram(programDTO);
                    program.setDepartment(department);
                    return program;
                })
                .collect(Collectors.toList());

        List<Program> programsToUpdate = new ArrayList<>();
        List<Program> programsToSave = new ArrayList<>();

        for (Program program : programs) {
            Program existingProgram = programRepository.findById(program.getProgram_id()).orElse(null);
            System.out.println(existingProgram);
            if (existingProgram != null) {
                // Skip if the description is the same (handle duplicate gracefully)
                if (!existingProgram.getDescription().equals(program.getDescription())) {
                    programsToUpdate.add(program);
                }
            } else {
                programsToSave.add(program);
            }
        }

        List<Program> savedPrograms = new ArrayList<>();
        if (!programsToSave.isEmpty()) {
            savedPrograms = programRepository.saveAll(programsToSave);
        }

        if (!programsToUpdate.isEmpty()) {
            programRepository.saveAll(programsToUpdate);
        }

        List<Program> finalPrograms = new ArrayList<>();
        finalPrograms.addAll(savedPrograms);
        finalPrograms.addAll(programsToUpdate);

        return finalPrograms.stream()
                .map(ProgramMapper::mapToProgramDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgramDTO> getAllPrograms() {
        List<Program> programs = programRepository.findAll();

        return programs.stream().map((program) -> ProgramMapper.mapToProgramDTO(program))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgramDTO> getAllProgramsByDepartment(String departmentID) {
        List<Program> programs = programRepository.findByDepartmentId(departmentID);

        return programs.stream().map((program) -> ProgramMapper.mapToProgramDTO(program))
                .collect(Collectors.toList());
    }
}
// @Override
// public List<ProgramDTO> uploadPrograms(List<ProgramDTO> programDTOs) {
// List<Program> program = programDTOs.stream().map(programDTO -> {
// Department department =
// departmentRepository.findById(programDTO.getDepartment_id())
// .orElseThrow(() -> new ResourceNotFoundException(
// "Department with ID" + programDTO.getDepartment_id() + "not found."));
// });
// return null;
// }
// @Override
// public ProgramDTO getProgramByID(String programID) {
// Program program = programRepository.findById(programID)
// .orElseThrow(() -> new ResourceNotFoundException("not existing" +
// programID));

// return ProgramMapper.mapToProgramDTO(program);
// }

// @Override
// public ProgramDTO updateDepartment(Integer programID, ProgramDTO
// updatedProgram) {
// Program program = programRepository.findById(programID)
// .orElseThrow(() -> new ResourceNotFoundException("Program not existing with
// given ID" + programID));

// Department department =
// departmentRepository.findById(updatedProgram.getDepartment_id())
// .orElseThrow(() -> new ResourceNotFoundException(
// "Department not existing with the given ID" +
// updatedProgram.getDepartment_id()));
// program.setDescription(updatedProgram.getDescription());
// program.setDepartment(department);
// program.setStatus(updatedProgram.getStatus());

// Program updatedEntity = programRepository.save(program);

// return ProgramMapper.mapToProgramDTO(updatedEntity);
// }