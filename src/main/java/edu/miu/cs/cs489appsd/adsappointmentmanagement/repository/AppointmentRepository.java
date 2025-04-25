package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDentist(Dentist dentist);
    List<Appointment> findByPatient(Patient patient);

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM Appointment a
        WHERE a.dentist.id = :dentistId AND a.date = :date AND a.appointmentTime = :time
    """)
    boolean isDentistBookedAt(Long dentistId, LocalDate date, LocalTime time);

    @Query("""
    SELECT a FROM Appointment a
    WHERE a.dentist.id = :dentistId 
    AND FUNCTION('YEAR', a.date) = :year 
    AND FUNCTION('WEEK', a.date) = :week
""")
    List<Appointment> findByDentistIdAndYearAndWeek(
            @Param("dentistId") Long dentistId,
            @Param("year") int year,
            @Param("week") int week
    );

    @Query("SELECT a FROM Appointment a WHERE a.patient.email = :email")
    List<Appointment> findByPatientEmail(String email);

    @Query("SELECT a FROM Appointment a WHERE a.dentist.email = :email")
    List<Appointment> findByDentistEmail(String email);

    @Query("""
    SELECT a FROM Appointment a
    WHERE (:status IS NULL OR a.status = :status)
      AND (:start IS NULL OR a.date >= :start)
      AND (:end IS NULL OR a.date <= :end)
      AND (:dentistId IS NULL OR a.dentist.id = :dentistId)
""")
    Page<Appointment> findWithFilters(@Param("status") String status,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end,
                                      @Param("dentistId") Long dentistId,
                                      Pageable pageable);


}
