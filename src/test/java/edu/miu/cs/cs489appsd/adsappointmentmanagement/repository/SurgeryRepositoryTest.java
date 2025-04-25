package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Surgery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SurgeryRepositoryTest {

    @Autowired
    private SurgeryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Surgery sample;

    @BeforeEach
    void setUp() {
        Address address = new Address("123 Main", "Unit 1", "Fairfield", "IA", 52557);
        address = entityManager.persistFlushFind(address);

        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("A-1001");
        appointment.setDate(LocalDate.now());
        appointment.setAppointmentTime(LocalTime.of(10, 0));
        appointment.setStatus("SCHEDULED");
        appointment = entityManager.persistFlushFind(appointment);

        sample = new Surgery();
        sample.setSurgeryName("Dental Implant");
        sample.setPhone("123-456-7890");
        sample.setSurgeryNumber("S-2001");
        sample.setLocation(address);
        sample.setAppointment(appointment);
    }


    @Test
    @DisplayName("Sanity check: Repository loads")
    void repositoryLoads() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test for saving entity")
    void whenSave_thenEntityIsPersisted() {
        var saved = repository.saveAndFlush(sample);
        assertNotNull(saved);
        assertEquals(sample, saved);
    }

    @Test
    @DisplayName("Test for finding entity by ID")
    void whenFindById_thenEntityIsReturned() {
        var persisted = entityManager.persistFlushFind(sample);
        var found = repository.findById(persisted.getId());
        assertTrue(found.isPresent());
        assertEquals(persisted.getId(), found.get().getId());
    }

    @Test
    @DisplayName("Test for deleting entity")
    void whenDelete_thenEntityIsRemoved() {
        var persisted = entityManager.persistFlushFind(sample);
        repository.deleteById(persisted.getId());
        entityManager.flush();
        var deleted = repository.findById(persisted.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    @DisplayName("Test for finding all entities")
    void whenFindAll_thenReturnList() {
        repository.saveAndFlush(sample);
        List<Surgery> all = repository.findAll();
        assertFalse(all.isEmpty());
    }
}
