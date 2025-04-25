package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Address sample;

    @BeforeEach
    void setUp() {
        sample = new Address();
        sample.setStreet("123 Main St");
        sample.setUnitNo("Unit 1A");
        sample.setCity("Fairfield");
        sample.setState("IA");
        sample.setZip(52557);
    }

    @Test
    @DisplayName("Sanity check: Repository loads")
    void repositoryLoads() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Save and retrieve address")
    void whenSave_thenCanRetrieve() {
        Address persisted = repository.saveAndFlush(sample);
        Optional<Address> found = repository.findById(persisted.getId());
        assertTrue(found.isPresent(), "Saved address should be retrievable");
        assertEquals(sample.getStreet(), found.get().getStreet());
    }

    @Test
    @DisplayName("Delete address successfully")
    void whenDelete_thenEntityIsRemoved() {
        Address persisted = entityManager.persistFlushFind(sample);
        repository.deleteById(persisted.getId());
        entityManager.flush();
        assertFalse(repository.findById(persisted.getId()).isPresent());
    }

    @Test
    @DisplayName("Unique constraint: UnitNo should be unique")
    void whenDuplicateUnitNo_thenFail() {
        Address saved = entityManager.persistFlushFind(sample);

        Address duplicate = new Address();
        duplicate.setStreet("456 Elm St");
        duplicate.setUnitNo(saved.getUnitNo()); // same unit number
        duplicate.setCity("Fairfield");
        duplicate.setState("IA");
        duplicate.setZip(52557);

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(duplicate);
        });
    }
}
