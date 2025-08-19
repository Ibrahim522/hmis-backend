package com.hmis.patient_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Patient {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int age;
    private String gender;
    private String contact;

    public Patient() {}

    public Patient(String name, int age, String gender, String contact) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
    }

    // Getters and setters
    // (or use Lombok to generate them)
}
