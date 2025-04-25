package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurgeryRepository extends JpaRepository<Surgery, Long> {
    @Query("""
    SELECT s FROM Surgery s 
    WHERE s.appointment.patient.email = :email
""")
    List<Surgery> findByPatientEmail(@Param("email") String email);

    @Query("""
    SELECT s FROM Surgery s 
    WHERE s.appointment.dentist.email = :email
""")
    List<Surgery> findByDentistEmail(@Param("email") String email);

}
