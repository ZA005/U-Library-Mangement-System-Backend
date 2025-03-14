package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.curriculum.DepartmentDTO;
import com.university.librarymanagementsystem.entity.curriculum.Department;

public interface DepartmentService {
    DepartmentDTO addDepartment(DepartmentDTO departmentDTO);

    List<DepartmentDTO> uploadDepartments(List<DepartmentDTO> departmentDTO);

    void updateMultipleDepartments(List<Department> departments);

    List<DepartmentDTO> getAllDepartments();

    DepartmentDTO getDepartmentById(String departmentID);

}