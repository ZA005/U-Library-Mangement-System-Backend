package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.curriculum.CurriculumDTO;
import com.university.librarymanagementsystem.entity.curriculum.Curriculum;
import com.university.librarymanagementsystem.entity.curriculum.Program;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.curriculum.CurriculumMapper;
import com.university.librarymanagementsystem.repository.curriculum.CurriculumRepository;
import com.university.librarymanagementsystem.repository.curriculum.ProgramRepository;
import com.university.librarymanagementsystem.service.curriculum.CurriculumService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {

    CurriculumRepository currRepo;
    ProgramRepository programRepository;

    @Override
    public List<CurriculumDTO> uploadCurriculum(List<CurriculumDTO> currDTO) {

        List<Curriculum> curriculums = currDTO.stream().map(curriculumDTO -> {
            Program program = programRepository.findById(curriculumDTO.getProgram_id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Program with ID " + curriculumDTO.getProgram_id() + " not found!"));

            System.out.println("Program ID: " + program.getId());
            Curriculum curriculum = CurriculumMapper.mapToCurriculum(curriculumDTO);
            curriculum.setProgram(program);
            return curriculum;
        }).collect(Collectors.toList());

        List<Curriculum> curriculumToUpdate = new ArrayList<>();
        List<Curriculum> curriculumToSave = new ArrayList<>();

        for (Curriculum curriculum : curriculums) {
            Curriculum existingCurriculum = currRepo.findById(curriculum.getId()).orElse(null);
            // System.out.println("Exisiting Curriculum " + existingCurriculum.getId());
            if (existingCurriculum != null) {
                // Skip if all relevant fields (revision_no, effectivity_sem, effectivity_sy)
                // are the same
                if (!(existingCurriculum.getRevision_no() == curriculum.getRevision_no() &&
                        existingCurriculum.getEffectivity_sem() == curriculum.getEffectivity_sem() &&
                        existingCurriculum.getEffectivity_sy().equals(curriculum.getEffectivity_sy()))) {
                    curriculumToUpdate.add(curriculum);
                }
            } else {
                curriculumToSave.add(curriculum);
            }

        }

        List<Curriculum> savedCurriculum = new ArrayList<>();

        if (!curriculumToSave.isEmpty()) {
            savedCurriculum = currRepo.saveAll(curriculumToSave);
        }

        if (!curriculumToUpdate.isEmpty()) {
            currRepo.saveAll(curriculumToUpdate);
        }

        List<Curriculum> finalCurriculum = new ArrayList<>();
        finalCurriculum.addAll(savedCurriculum);
        finalCurriculum.addAll(curriculumToUpdate);

        System.out.println("To Save: " + curriculumToSave.size());
        System.out.println("To Update: " + curriculumToUpdate.size());
        System.out.println("Saved: " + savedCurriculum);

        return finalCurriculum.stream()
                .map(CurriculumMapper::mapToCurriculumDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CurriculumDTO addCurriculum(CurriculumDTO currDTO) {
        Curriculum curr = CurriculumMapper.mapToCurriculum(currDTO);

        return CurriculumMapper.mapToCurriculumDTO(curr);
    }

    @Override
    public List<CurriculumDTO> getAllCurriculum() {
        List<Curriculum> curriculums = currRepo.findAll();

        return curriculums.stream().map((curriculum) -> CurriculumMapper.mapToCurriculumDTO(curriculum))
                .collect(Collectors.toList());
    }

    @Override
    public List<CurriculumDTO> getAllCurriculumByProgram(Integer programId) {
        List<Curriculum> curriculums = currRepo.findByProgramId(programId);

        return curriculums.stream().map((curriculum) -> CurriculumMapper.mapToCurriculumDTO(curriculum))
                .collect(Collectors.toList());
    }
}
// currDTO.forEach(dto -> {
// System.out.println(dto.getCurr_id());
// System.out.println(dto.getProgram_id());
// System.out.println(dto.getRevision_no());
// System.out.println(dto.getEffectivity_sem());
// System.out.println(dto.getEffectivity_sy());
// System.out.println(dto.getStatus());
// });