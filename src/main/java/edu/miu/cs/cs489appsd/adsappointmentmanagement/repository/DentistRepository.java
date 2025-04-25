package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
        Optional<Dentist> findByEmail(String email);
}
