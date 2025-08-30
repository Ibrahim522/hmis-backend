package com.hmis.repository;
import java.util.List;
import com.hmis.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
     List<Appointment> findByDoctorId(Long doctorId);
}
