package com.university.librarymanagementsystem.controller.curriculum;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.curriculum.CurriculumDTO;
import com.university.librarymanagementsystem.service.curriculum.CurriculumService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/public/curriculums")
public class CurriculumController {

    private CurriculumService curriculumService;

    @PostMapping("/upload")
    public ResponseEntity<List<CurriculumDTO>> uploadCurriculum(@RequestBody List<CurriculumDTO> curriculumDTO) {
        List<CurriculumDTO> curriculums = curriculumService.uploadCurriculum(curriculumDTO);

        return new ResponseEntity<>(curriculums, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CurriculumDTO>> getAllCurriculum() {
        List<CurriculumDTO> curriculumList = curriculumService.getAllCurriculum();

        return ResponseEntity.ok(curriculumList);
    }

    @GetMapping("/program/{id}")
    public ResponseEntity<List<CurriculumDTO>> getAllCurriculumByProgram(@PathVariable("id") Integer programId) {
        List<CurriculumDTO> curriculums = curriculumService.getAllCurriculumByProgram(programId);

        return ResponseEntity.ok(curriculums);
    }
}