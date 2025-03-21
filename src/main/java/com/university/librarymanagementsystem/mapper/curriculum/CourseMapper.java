package com.university.librarymanagementsystem.mapper.curriculum;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.curriculum.CourseDTO;
import com.university.librarymanagementsystem.entity.curriculum.Course;
import com.university.librarymanagementsystem.entity.curriculum.Curriculum;

@Component
public class CourseMapper {

    public static CourseDTO mapToCourseDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getCurriculum().getId(),
                course.getCurriculum().getRevision_no(),
                course.getCurriculum().getProgram().getId(),
                course.getCurriculum().getProgram().getCode(),
                course.getCurriculum().getProgram().getDescription(),
                course.getCourse_code(),
                course.getCourse_name(),
                course.getYear_level(),
                course.getSem(),
                course.getMajor());

    }

    public static Course maptoCourse(CourseDTO courseDTO) {

        Curriculum curr = new Curriculum();
        curr.setId(courseDTO.getCurr_id());
        return new Course(
                courseDTO.getId(),
                curr,
                courseDTO.getCourse_code(),
                courseDTO.getCourse_name(),
                courseDTO.getYear_level(),
                courseDTO.getSem(),
                courseDTO.getMajor());

    }
}