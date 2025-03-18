package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.curriculum.CourseDTO;
import com.university.librarymanagementsystem.entity.curriculum.Course;
import com.university.librarymanagementsystem.entity.curriculum.Curriculum;
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
        List<Course> courseToUpdate = new ArrayList<>();
        List<Course> courseToSave = new ArrayList<>();
        List<Course> courseToLink = new ArrayList<>();

        for (CourseDTO courseDTO : coursesDTO) {
            Curriculum curriculum = currRepository.findById(courseDTO.getCurr_id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Curriculum with ID: " + courseDTO.getCurr_id() + " not found!"));

            String progCode = curriculum.getProgram().getCode();
            String courseCode = courseDTO.getCourse_code();

            System.out.println("PROGRAM CODE:" + progCode);
            System.out.println("COURSE CODE:" + courseCode);
            String cleanedProgCode = progCode.replace("BS", "");

            System.out.println("NEW PROGRAM CODE:" + cleanedProgCode);

            int isMajor = courseCode.contains(cleanedProgCode) ? 1 : 0;

            Optional<Course> existingCourse = courseRepository.findByCourseCodeSingle(courseCode);

            if (existingCourse.isPresent()) {
                Course course = existingCourse.get();

                Optional<Course> courseInCurriculum = courseRepository.findByCourseCodeAndCurriculum(
                        courseCode, courseDTO.getCurr_id());

                if (courseInCurriculum.isEmpty()) {
                    Course linkedCourse = new Course();
                    linkedCourse.setCourse_code(course.getCourse_code());
                    linkedCourse.setCourse_name(course.getCourse_name());
                    linkedCourse.setYear_level(course.getYear_level());
                    linkedCourse.setSem(course.getSem());
                    linkedCourse.setCurriculum(curriculum);
                    linkedCourse.setMajor(isMajor);

                    courseToLink.add(linkedCourse);
                }
            } else {
                Course newCourse = CourseMapper.maptoCourse(courseDTO);
                newCourse.setCurriculum(curriculum);
                newCourse.setMajor(isMajor);

                Optional<Course> existingMajor = courseRepository.findByCourseCodeAndCurriculum(
                        courseCode, courseDTO.getCurr_id());

                if (existingMajor.isPresent()) {
                    Course majorCourse = existingMajor.get();

                    if (!majorCourse.getCourse_name().equals(newCourse.getCourse_name()) ||
                            majorCourse.getYear_level() != newCourse.getYear_level()) {
                        majorCourse.setCourse_name(newCourse.getCourse_name());
                        majorCourse.setYear_level(newCourse.getYear_level());
                        majorCourse.setSem(newCourse.getSem());

                        courseToUpdate.add(majorCourse);
                    }
                } else {
                    courseToSave.add(newCourse);
                }
            }
        }

        List<Course> savedCourses = new ArrayList<>();

        if (!courseToSave.isEmpty()) {
            savedCourses = courseRepository.saveAll(courseToSave);
        }

        if (!courseToUpdate.isEmpty()) {
            courseRepository.saveAll(courseToUpdate);
        }

        if (!courseToLink.isEmpty()) {
            courseRepository.saveAll(courseToLink);
        }

        List<Course> finalCourses = new ArrayList<>();
        finalCourses.addAll(savedCourses);
        finalCourses.addAll(courseToUpdate);
        finalCourses.addAll(courseToLink);

        return finalCourses.stream().map(CourseMapper::mapToCourseDTO).collect(Collectors.toList());
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