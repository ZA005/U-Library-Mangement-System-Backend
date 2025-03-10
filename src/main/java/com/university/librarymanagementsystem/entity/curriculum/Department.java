package com.university.librarymanagementsystem.entity.curriculum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @Column(name = "dept_id", length = 2, nullable = false)
    private String id;

    @Column(name = "dept_name", length = 100)
    private String name;

    @Column(name = "dept_code", length = 10)
    private String code;

    @Override
    public String toString() {
        return "Department{" +
                "deptId='" + id + '\'' +
                ", deptName='" + name + '\'' +
                ", deptCode='" + code + '\'' +
                '}';
    }
}