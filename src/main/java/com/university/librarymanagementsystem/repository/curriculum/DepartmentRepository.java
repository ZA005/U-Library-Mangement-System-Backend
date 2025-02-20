package com.university.librarymanagementsystem.repository.curriculum;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.curriculum.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM departments d WHERE d.dept_name=:name", nativeQuery = true)
    boolean existsByName(@Param("name") String name);

    // @Query(value = "SELECT * FROM departments WHERE dept_id = : dept_id ",
    // nativeQuery = true)
    // Optional<Department> findByDeptId(@Param("dept_id") String dept_id);

    @Query(value = "SELECT * FROM departments WHERE dept_name = :departmentName", nativeQuery = true)
    Optional<Department> findByName(@Param("departmentName") String departmentName);
}