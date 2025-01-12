package com.byrybdyk.lb1.model;

import com.byrybdyk.lb1.model.enums.Color;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull(message = "Имя не может быть пустым")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color", nullable = false)
    @NotNull(message = "Цвет глаз не может быть пустым")
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color")
    private Color hairColor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    @NotNull(message = "Местоположение не может быть пустым")
    private Location location;

    @Column(nullable = false)
    @Min(value = 0, message = "Вес должен быть больше 0")
    private double weight;

    @Column(name = "passport_id", nullable = false)
    @NotNull(message = "Номер паспорта не может быть пустым")
    private Integer passportID;

    @Column(name = "dtype", nullable = false)
    private String dtype = "DEFAULT_PERSON_TYPE";

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    // Геттеры и сеттеры
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

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Integer getPassportID() {
        return passportID;
    }

    public void setPassportID(Integer passportID) {
        this.passportID = passportID;
    }
}
