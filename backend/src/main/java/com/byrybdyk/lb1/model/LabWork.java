package com.byrybdyk.lb1.model;

import com.byrybdyk.lb1.model.enums.Difficulty;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "lab_work")
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_type")
    private Difficulty difficulty;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @Column(name = "minimal_point", nullable = false)
    private Float minimalPoint;

    @Column(name = "personal_qualities_minimum", nullable = false)
    private double personalQualitiesMinimum;

    @Column(name = "personal_qualities_maximum")
    private Float personalQualitiesMaximum;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner_id;


    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Float getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Float minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public double getPersonalQualitiesMinimum() {
        return personalQualitiesMinimum;
    }

    public void setPersonalQualitiesMinimum(double personalQualitiesMinimum) {
        this.personalQualitiesMinimum = personalQualitiesMinimum;
    }

    public Float getPersonalQualitiesMaximum() {
        return personalQualitiesMaximum;
    }

    public void setPersonalQualitiesMaximum(Float personalQualitiesMaximum) {
        this.personalQualitiesMaximum = personalQualitiesMaximum;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public User getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(User owner_id) {
        this.owner_id = owner_id;
    }
}
