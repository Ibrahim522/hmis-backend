package com.hmis.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmis.model.Appointment;
import com.hmis.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Clean DB before each test
        appointmentRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void testBookAppointment() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(1L);
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(LocalDateTime.parse("2025-09-21T15:00"));

        mockMvc.perform(post("/appointments/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId", is(1)))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.appointmentDate").value("2025-09-21T15:00"));
    }

    @Test
    @WithMockUser
    public void testGetAllAppointments() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(1L);
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(LocalDateTime.parse("2025-09-21T15:00"));
        appointmentRepository.save(appointment);
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void testGetAppointmentsByDoctor() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setDoctorId(1L);
        appointment1.setPatientId(1L);
        appointment1.setAppointmentDate(LocalDateTime.parse("2025-09-21T15:00"));
        appointmentRepository.save(appointment1);
        Appointment appointment2 = new Appointment();
        appointment2.setDoctorId(2L);
        appointment2.setPatientId(2L);
        appointment2.setAppointmentDate(LocalDateTime.parse("2025-09-21T15:00"));
        appointmentRepository.save(appointment2);

        mockMvc.perform(get("/appointments/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].doctorId", is(1)));
    }

    @Test
    @WithMockUser
    public void testCancelAppointment_Success() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(1L);
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(LocalDateTime.parse("2025-09-21T15:00"));

        Appointment saved = appointmentRepository.save(appointment);

        mockMvc.perform(delete("/appointments/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void testCancelAppointment_NotFound() throws Exception {
        mockMvc.perform(delete("/appointments/99999"))
                .andExpect(status().isNotFound());
    }
}
