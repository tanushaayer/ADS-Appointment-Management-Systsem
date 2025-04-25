package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.OfficeManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeManagerRepository extends JpaRepository<OfficeManager, Long> {

    Optional<OfficeManager> findByEmail(String email);
}
