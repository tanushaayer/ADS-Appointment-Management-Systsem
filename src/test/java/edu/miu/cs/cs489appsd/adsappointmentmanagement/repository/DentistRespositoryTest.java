package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DentistRepositoryTest {

    @Autowired
    private DentistRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Dentist sample;

    @BeforeEach
    void setUp() {
        sample = new Dentist();
        sample.setFirstName("John");
        sample.setLastName("Doe");
        sample.setEmail("john.doe@example.com");
        sample.setPhoneNumber("123-456-7890");
        sample.setPassword("securePassword123");
        sample.setRole(RoleEnum.DENTIST);
        sample.setSpecialization("Orthodontist");
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
        assertEquals(sample.getEmail(), saved.getEmail());
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
        List<Dentist> all = repository.findAll();
        assertFalse(all.isEmpty());
    }
}
