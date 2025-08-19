package com.hmis.patient_service.controller;

import com.hmis.patient_service.model.Patient;
import com.hmis.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public Patient registerPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
