package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.curriculum.DepartmentDTO;
import com.university.librarymanagementsystem.entity.curriculum.Department;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.curriculum.DepartmentMapper;
import com.university.librarymanagementsystem.repository.curriculum.DepartmentRepository;
import com.university.librarymanagementsystem.service.curriculum.DepartmentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.mapToDepartment(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);

        return DepartmentMapper.mapToDepartmentDTO(savedDepartment);
    }

    @Override
    public List<DepartmentDTO> uploadDepartments(List<DepartmentDTO> departmentDTOs) {
        List<Department> departments = departmentDTOs.stream()
                .map(DepartmentMapper::mapToDepartment)
                .collect(Collectors.toList());

        List<Department> departmentsToUpdate = new ArrayList<>();
        List<Department> departmentsToSave = new ArrayList<>();

        for (Department department : departments) {
            // Check if department ID exists
            Department existingDepartment = departmentRepository.findById(department.getId()).orElse(null);

            if (existingDepartment != null) {
                // If the department exists, check if the name is the same
                if (!existingDepartment.getName().equals(department.getName())) {
                    // If the name is different, prepare for update
                    departmentsToUpdate.add(department);
                }
                // If the name is the same, skip the duplicate
            } else {
                // If the department does not exist, add it to the save list
                departmentsToSave.add(department);
            }
        }

        // Save new departments
        List<Department> savedDepartments = new ArrayList<>();
        if (!departmentsToSave.isEmpty()) {
            savedDepartments = departmentRepository.saveAll(departmentsToSave);
        }

        // Save updated departments
        if (!departmentsToUpdate.isEmpty()) {
            departmentRepository.saveAll(departmentsToUpdate); // Saving updated departments
        }

        // Combine saved and updated departments
        List<Department> finalDepartments = new ArrayList<>();
        finalDepartments.addAll(savedDepartments);
        finalDepartments.addAll(departmentsToUpdate);

        return finalDepartments.stream()
                .map(DepartmentMapper::mapToDepartmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(String departmentID) {
        Department department = departmentRepository.findById(departmentID)
                .orElseThrow(() -> new ResourceNotFoundException("not exisiting" +
                        departmentID));

        return DepartmentMapper.mapToDepartmentDTO(department);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream().map((department) -> DepartmentMapper.mapToDepartmentDTO(department))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMultipleDepartments(List<Department> departments) {
        for (Department department : departments) {
            // Check if department exists by ID
            if (departmentRepository.existsById(department.getId())) {
                // Fetch the existing department from the database
                Department existingDepartment = departmentRepository.findById(department.getId())
                        .orElseThrow(() -> new RuntimeException("Department not found with ID: " + department.getId()));

                // Update fields as needed (e.g., name, etc.)
                existingDepartment.setName(department.getName());
                existingDepartment.setCode(department.getCode());
                // Add more fields here as required for updating

                // Save the updated department
                departmentRepository.save(existingDepartment);
            } else {
                // If department doesn't exist, you can handle it accordingly (e.g., throw an
                // exception)
                throw new RuntimeException("Department not found with ID: " + department.getId());
            }
        }
    }

}