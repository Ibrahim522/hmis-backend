package com.hmis.controller;

import com.hmis.model.Patient;
import com.hmis.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.security.test.context.support.WithMockUser;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientRepository patientRepository;

    @Test
    public void testRegisterPatient() throws Exception {
        Patient mockPatient = new Patient();
        mockPatient.setId(1L);
        mockPatient.setFirstName("John");
        mockPatient.setLastName("Doe");     

        when(patientRepository.save(any(Patient.class))).thenReturn(mockPatient);

        String patientJson = "{\"firstName\": \"John\", \"lastName\": \"Doe\"}";

        mockMvc.perform(post("/patients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testGetAllPatients() throws Exception {
        Patient mockPatient = new Patient();
        mockPatient.setId(1L);
        mockPatient.setFirstName("John");
        mockPatient.setLastName("Doe");

        when(patientRepository.findAll()).thenReturn(List.of(mockPatient));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }
}
