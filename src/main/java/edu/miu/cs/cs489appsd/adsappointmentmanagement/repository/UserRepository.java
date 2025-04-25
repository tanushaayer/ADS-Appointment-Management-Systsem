package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
