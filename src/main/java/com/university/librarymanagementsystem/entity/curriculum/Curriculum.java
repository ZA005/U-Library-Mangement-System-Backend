package com.university.librarymanagementsystem.entity.curriculum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "curriculum")
@Data
public class Curriculum {
    @Id
    @Column(name = "curr_id", length = 2, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "revision_no", nullable = false)
    private int revision_no;

    @Column(name = "effectivity_sem", nullable = false)
    private int effectivity_sem;

    @Column(name = "effectivity_sy", length = 4)
    private String effectivity_sy;

    @Column(name = "status", nullable = false)
    private byte status;
}