package com.university.librarymanagementsystem.controller.curriculum;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.curriculum.DepartmentDTO;
import com.university.librarymanagementsystem.service.curriculum.DepartmentService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
@RequestMapping("/public/departments")
public class DepartmentController {

    private DepartmentService departmentService;

    // ADDING OF DEPARTMENT
    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO savedDepartment = departmentService.addDepartment(departmentDTO);
        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

    // ADDING OF DEPARTMENTS VIA UPLOAD
    @PostMapping("/upload")
    public ResponseEntity<List<DepartmentDTO>> uploadDepartments(@RequestBody List<DepartmentDTO> deptDTO) {
        List<DepartmentDTO> uploadedDepartments = departmentService.uploadDepartments(deptDTO);

        return new ResponseEntity<>(uploadedDepartments, HttpStatus.CREATED);
    }

    // FETCHING OF DEPARTMENT BY ID
    @GetMapping("{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentByID(@PathVariable("id") String departmentID) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(departmentID);

        return ResponseEntity.ok(departmentDTO);
    }

    // FETCHING ALL DEPARTMENTS
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(departments);
    }

    // // UPDATING DEPARTMENT
    // @PutMapping("{id}")
    // public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable("id")
    // Integer departmentID,
    // @RequestBody DepartmentDTO updatedDepartment) {

    // DepartmentDTO departmentDTO =
    // departmentService.updateDepartment(departmentID, updatedDepartment);

    // return ResponseEntity.ok(departmentDTO);
    // }
}