package com.hmis.doctor_service.controller;

import com.hmis.doctor_service.model.Doctor;
import com.hmis.doctor_service.repository.DoctorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorRepository repository;

    public DoctorController(DoctorRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Doctor registerDoctor(@RequestBody Doctor doctor) {
        return repository.save(doctor);
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return repository.findAll();
    }
}
