package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.curriculum.CurriculumDTO;

public interface CurriculumService {

    CurriculumDTO addCurriculum(CurriculumDTO currDTO);

    List<CurriculumDTO> uploadCurriculum(List<CurriculumDTO> currDTO);

    List<CurriculumDTO> getAllCurriculum();

    List<CurriculumDTO> getAllCurriculumByProgram(Integer programId);
}