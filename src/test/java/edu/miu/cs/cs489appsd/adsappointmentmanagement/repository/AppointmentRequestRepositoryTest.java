package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.AppointmentRequest;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppointmentRequestRepositoryTest {

    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    private TestEntityManager entityManager;

    private AppointmentRequest request;
    private Patient savedPatient;

    @BeforeEach
    void setUp() {
        // Create and save a valid patient (inherited from User, so all required fields must be set)
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Doe");
        patient.setEmail("alice@doe.com");
        patient.setPhoneNumber("555-9999");
        patient.setDob(LocalDate.of(1991, 2, 3));
        patient.setPassword("secret123");
        patient.setRole(RoleEnum.PATIENT);
        savedPatient = entityManager.persistAndFlush(patient);

        // Create a test appointment request
        request = new AppointmentRequest();
        request.setDate(LocalDate.now());
        request.setTime(LocalTime.of(9, 0));
        request.setReason("Checkup");
        request.setStatus("PENDING");
        request.setPatient(savedPatient);
    }

    @Test
    @DisplayName("Save and retrieve appointment request")
    void testSaveAndRetrieve() {
        appointmentRequestRepository.saveAndFlush(request);
        List<AppointmentRequest> all = appointmentRequestRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals("PENDING", all.get(0).getStatus());
    }

    @Test
    @DisplayName("Find by patient")
    void testFindByPatient() {
        appointmentRequestRepository.saveAndFlush(request);
        List<AppointmentRequest> results = appointmentRequestRepository.findByPatientId(savedPatient.getId());
        assertEquals(1, results.size());
    }
}
