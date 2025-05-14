package com.university.librarymanagementsystem.entity.curriculum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "curriculum")
public class Curriculum {

    @Id
    @Column(name = "curr_id", length = 2, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "revision_no", nullable = false)
    private int revision_no;

    @Column(name = "effectivity_sy", length = 4)
    private String effectivity_sy;

    @Column(name = "status", nullable = false)
    private byte status;

    public Curriculum() {
    }

    public Curriculum(String id, Program program, int revision_no, String effectivity_sy, byte status) {
        this.id = id;
        this.program = program;
        this.revision_no = revision_no;
        this.effectivity_sy = effectivity_sy;
        this.status = status;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public int getRevision_no() {
        return revision_no;
    }

    public void setRevision_no(int revision_no) {
        this.revision_no = revision_no;
    }

    public String getEffectivity_sy() {
        return effectivity_sy;
    }

    public void setEffectivity_sy(String effectivity_sy) {
        this.effectivity_sy = effectivity_sy;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
