package com.byrybdyk.lb1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "discipline")
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "practice_hours", nullable = false)
    private long practiceHours;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPracticeHours() {
        return practiceHours;
    }

    public void setPracticeHours(long practiceHours) {
        this.practiceHours = practiceHours;
    }
}