package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Patient patient;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Toothville");
        address.setState("IA");
        address.setZip(12345);

        patient = new Patient();
        patient.setFirstName("Bob");
        patient.setLastName("Molar");
        patient.setPhoneNumber("555-112-3333");
        patient.setEmail("bob@example.com");
        patient.setPassword("password123");
        patient.setDob(LocalDate.of(1990, 5, 15));
        patient.setAddress(address);
    }

    @Test
    @DisplayName("Should save and retrieve a patient successfully")
    void saveAndRetrievePatient() {
        Patient saved = patientRepository.saveAndFlush(patient);
        Optional<Patient> retrieved = patientRepository.findById(saved.getId());

        assertTrue(retrieved.isPresent());
        assertEquals("Bob", retrieved.get().getFirstName());
        assertEquals("bob@example.com", retrieved.get().getEmail());
    }

    @Test
    @DisplayName("Should find patient by email")
    void findByEmail() {
        patientRepository.saveAndFlush(patient);
        Optional<Patient> result = patientRepository.findByEmail("bob@example.com");
        assertTrue(result.isPresent());
        assertEquals("Bob", result.get().getFirstName());
    }

    @Test
    @DisplayName("Should fail when saving patient with null email")
    void shouldFailForNullEmail() {
        Patient invalid = new Patient();
        invalid.setFirstName("Jane");
        invalid.setLastName("Doe");
        invalid.setPassword("test123");
        invalid.setPhoneNumber("555-1111");
        invalid.setRole(RoleEnum.PATIENT);
        invalid.setDob(LocalDate.of(1990, 1, 1));
        invalid.setAddress(new Address("123 Main", "Apt 1", "Fairfield", "IA", 52557));

        // Deliberately not setting email to trigger error
        assertThrows(DataIntegrityViolationException.class, () -> {
            patientRepository.saveAndFlush(invalid);
        });
    }

}
