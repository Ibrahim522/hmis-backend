package com.hmis.controller;

import com.hmis.model.Doctor;
import com.hmis.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorRepository doctorRepository;

    @Test
    public void testRegisterDoctor() throws Exception {
        Doctor mockDoctor = new Doctor();
        mockDoctor.setId(1L);
        mockDoctor.setFirstName("John");
        mockDoctor.setLastName("Doe");

        when(doctorRepository.save(any(Doctor.class))).thenReturn(mockDoctor);

        String doctorJson = "{\"firstName\": \"John\", \"lastName\": \"Doe\"}";

        mockMvc.perform(post("/doctors/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(doctorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testGetAllDoctors() throws Exception {
        Doctor mockDoctor = new Doctor();
        mockDoctor.setId(1L);
        mockDoctor.setFirstName("John");
        mockDoctor.setLastName("Doe");

        when(doctorRepository.findAll()).thenReturn(List.of(mockDoctor));

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }
}
