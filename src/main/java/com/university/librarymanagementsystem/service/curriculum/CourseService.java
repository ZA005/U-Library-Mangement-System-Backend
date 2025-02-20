package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.curriculum.CourseDTO;

public interface CourseService {
    CourseDTO addCourse(CourseDTO courseDTO);

    List<CourseDTO> uploadCourses(List<CourseDTO> courseDTO);

    // List<CourseDTO> addSubjects(List<CourseDTO> subjectDTOs);

    // CourseDTO getSubjectByID(Integer subjectID);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> getAllCourseByProgram(Integer programId);

    List<CourseDTO> getAllCourseByRevision(Integer revisionNo);

    // List<CourseDTO> getAllSubjectsByProgram(Integer programId);

    // CourseDTO updateSubject(Integer subjectID, CourseDTO updateSubject);
}
