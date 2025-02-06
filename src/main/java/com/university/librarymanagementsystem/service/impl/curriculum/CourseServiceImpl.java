package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.curriculum.CourseDTO;
import com.university.librarymanagementsystem.entity.curriculum.Course;
import com.university.librarymanagementsystem.entity.curriculum.Curriculum;
import com.university.librarymanagementsystem.exception.DuplicateEntryException;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.curriculum.CourseMapper;
import com.university.librarymanagementsystem.repository.curriculum.CurriculumRepository;
import com.university.librarymanagementsystem.repository.curriculum.CourseRepository;
import com.university.librarymanagementsystem.service.curriculum.CourseService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    // private ProgramRepository programRepository;
    private CurriculumRepository currRepository;

    @Override
    public CourseDTO addCourse(CourseDTO courseDTO) {
        Curriculum curriculum = currRepository.findById(courseDTO.getCurr_id()).orElseThrow(
                () -> new ResourceNotFoundException("Curriculum not found for id:" + courseDTO.getCurr_id()));

        Course course = CourseMapper.maptoCourse(courseDTO);

        // boolean isExisting =
        // courseRepository.existsByName(courseDTO.getCourse_name());

        // if (isExisting) {
        // throw new DuplicateEntryException("A Course with the same name is already
        // existed!");
        // }

        course.setCurriculum(curriculum);

        return CourseMapper.mapToCourseDTO(course);
    }

    @Override
    public List<CourseDTO> uploadCourses(List<CourseDTO> coursesDTO) {
        coursesDTO.forEach(dto -> {
            System.out.println("COURSE ID: " + dto.getCourse_id());
        });

        List<Course> courses = coursesDTO.stream().map(courseDTO -> {
            Curriculum curriculum = currRepository.findById(courseDTO.getCurr_id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Curriculum with ID: " + courseDTO.getCurr_id() + " not found!"));

            Course course = CourseMapper.maptoCourse(courseDTO);

            course.setCurriculum(curriculum);

            return course;
        }).collect(Collectors.toList());

        List<Course> courseToUpdate = new ArrayList<>();
        List<Course> courseToSave = new ArrayList<>();

        for (Course course : courses) {
            Course existingCourse = courseRepository.findById(course.getCourse_id()).orElse(null);

            if (existingCourse != null) {
                // Skip if the course code, name, and year level are the same
                if (!existingCourse.getCourse_code().equals(course.getCourse_code()) ||
                        !existingCourse.getCourse_name().equals(course.getCourse_name()) ||
                        existingCourse.getYear_level() != course.getYear_level()) {
                    courseToUpdate.add(course);
                }
            } else {
                courseToSave.add(course);
            }

        }

        List<Course> savedCourse = new ArrayList<>();

        if (!courseToSave.isEmpty()) {
            savedCourse = courseRepository.saveAll(courseToSave);
        }

        if (!courseToUpdate.isEmpty()) {
            courseRepository.saveAll(courseToUpdate);
        }

        List<Course> finalCourse = new ArrayList<>();

        finalCourse.addAll(savedCourse);
        finalCourse.addAll(courseToUpdate);

        return finalCourse.stream().map(CourseMapper::mapToCourseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map((course) -> CourseMapper.mapToCourseDTO(course)).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllCourseByProgram(Integer programId) {
        List<Course> course = courseRepository.findByProgramId(programId);

        return course.stream().map((courses) -> CourseMapper.mapToCourseDTO(courses)).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllCourseByRevision(Integer revisionNo) {
        List<Course> course = courseRepository.findByRevisionNo(revisionNo);

        return course.stream().map((courses) -> CourseMapper.mapToCourseDTO(courses)).collect(Collectors.toList());
    }
}