package com.byrybdyk.lb1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private float x;

    @Column(nullable = false)
    private int y;

    // Getters and Setters
}