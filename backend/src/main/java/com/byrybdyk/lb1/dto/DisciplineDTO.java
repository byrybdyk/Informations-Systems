package com.byrybdyk.lb1.dto;

public class DisciplineDTO {
    private Long id;
    private String name;
    private long practiceHours;

    public DisciplineDTO() {}

    public DisciplineDTO(Long id, String name, long practiceHours) {
        this.id = id;
        this.name = name;
        this.practiceHours = practiceHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
