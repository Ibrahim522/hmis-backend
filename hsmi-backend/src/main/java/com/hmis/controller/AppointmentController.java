package com.hmis.controller;

import com.hmis.model.Appointment;
import com.hmis.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/book")
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            return ResponseEntity.notFound().build();
        }
        appointmentRepository.deleteById(appointmentId);
        return ResponseEntity.noContent().build();
    }

    // Fetch all patients from Patient Service
    @GetMapping("/patients")
    public ResponseEntity<String> getPatients() {
        String patientsUrl = "http://localhost:8081/patients";
        String patients = restTemplate.getForObject(patientsUrl, String.class);
        return ResponseEntity.ok(patients);
    }

    // Fetch all doctors from Doctor Service
    @GetMapping("/doctors")
    public ResponseEntity<String> getDoctors() {
        String doctorsUrl = "http://localhost:8081/doctors";
        String doctors = restTemplate.getForObject(doctorsUrl, String.class);
        return ResponseEntity.ok(doctors);
    }
}
