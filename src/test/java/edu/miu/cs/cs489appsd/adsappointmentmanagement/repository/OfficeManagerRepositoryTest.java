package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.OfficeManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OfficeManagerRepositoryTest {

    @Autowired
    private OfficeManagerRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private OfficeManager sample;

    @BeforeEach
    void setUp() {
        sample = new OfficeManager();
        sample.setFirstName("Anna");
        sample.setLastName("Smith");
        sample.setEmail("anna.manager@example.com");
        sample.setPassword("securePassword123");
        sample.setPhoneNumber("999-999-9999");
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
        List<OfficeManager> all = repository.findAll();
        assertFalse(all.isEmpty());
    }
}
