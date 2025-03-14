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

import com.university.librarymanagementsystem.dto.curriculum.CourseDTO;
import com.university.librarymanagementsystem.exception.DuplicateEntryException;
import com.university.librarymanagementsystem.service.curriculum.CourseService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/public/courses")
public class CourseController {

    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> addSubject(@RequestBody CourseDTO subjectDTO) {
        try {
            System.out.println("Received Program ID: " + subjectDTO.getProgram_id());
            CourseDTO savedSubject = courseService.addCourse(subjectDTO);
            return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
        } catch (DuplicateEntryException e) {
            // Return a custom error response for duplicate subjects
            return ResponseEntity.badRequest().body(new ErrorResponse("Duplicate subject: " + e.getMessage()));
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<List<CourseDTO>> uploadCourse(@RequestBody List<CourseDTO> courseDTO) {
        List<CourseDTO> courses = courseService.uploadCourses(courseDTO);

        return new ResponseEntity<>(courses, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/program/{id}")
    public ResponseEntity<List<CourseDTO>> gettAllCourseByProgram(@PathVariable("id") Integer programId) {
        List<CourseDTO> courses = courseService.getAllCourseByProgram(programId);

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/program/curriculum/rev/{id}")
    public ResponseEntity<List<CourseDTO>> gettAllCourseByRevision(@PathVariable("id") Integer revisionNo) {
        List<CourseDTO> courses = courseService.getAllCourseByRevision(revisionNo);

        return ResponseEntity.ok(courses);
    }

    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}