package com.university.librarymanagementsystem.mapper.curriculum;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.curriculum.CurriculumDTO;
import com.university.librarymanagementsystem.entity.curriculum.Curriculum;
import com.university.librarymanagementsystem.entity.curriculum.Program;

@Component
public class CurriculumMapper {
    public static CurriculumDTO mapToCurriculumDTO(Curriculum curr) {
        return new CurriculumDTO(
                curr.getId(),
                curr.getProgram().getId(),
                curr.getProgram().getCode(),
                curr.getProgram().getDescription(),
                curr.getRevision_no(),
                curr.getEffectivity_sem(),
                curr.getEffectivity_sy(),
                curr.getStatus());
    }

    public static Curriculum mapToCurriculum(CurriculumDTO currDTO) {
        Program program = new Program();
        program.setId(currDTO.getProgram_id());
        return new Curriculum(
                currDTO.getId(),
                program,
                currDTO.getRevision_no(),
                currDTO.getEffectivity_sem(),
                currDTO.getEffectivity_sy(),
                currDTO.getStatus());
    }
}