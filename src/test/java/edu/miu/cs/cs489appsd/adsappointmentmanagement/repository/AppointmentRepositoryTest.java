package edu.miu.cs.cs489appsd.adsappointmentmanagement.repository;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SurgeryRepository surgeryRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @Transactional
    @DisplayName("Save and retrieve appointment with linked dentist, patient, surgery, and address")
    void testSaveAndRetrieveAppointmentWithSurgery() {
        // Setup
        Dentist dentist = createAndSaveDentist();
        Patient patient = createAndSavePatient();
        Address address = createAndSaveAddress();
        Appointment appointment = createAndSaveAppointment(dentist, patient);
        Surgery surgery = createAndSaveSurgery(address, appointment);

        // Link surgery back to appointment
        appointment.setSurgery(surgery);
        appointmentRepository.save(appointment);

        // Test
        Appointment found = appointmentRepository.findById(appointment.getId()).orElse(null);

        // Assertions
        assertThat(found).isNotNull();
        assertThat(found.getDentist()).isNotNull();
        assertThat(found.getDentist().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(found.getPatient()).isNotNull();
        assertThat(found.getPatient().getEmail()).isEqualTo("alice.smith@example.com");
        assertThat(found.getSurgery()).isNotNull();
        assertThat(found.getSurgery().getSurgeryNumber()).isEqualTo("SUR-001");
        assertThat(found.getSurgery().getLocation().getCity()).isEqualTo("Fairfield");
    }

    // --------------------------
    // Helper Methods
    // --------------------------

    private Dentist createAndSaveDentist() {
        Dentist dentist = new Dentist();
        dentist.setFirstName("John");
        dentist.setLastName("Doe");
        dentist.setEmail("john.doe@example.com");
        dentist.setPassword("secure123");
        dentist.setPhoneNumber("1112223333");
        dentist.setRole(RoleEnum.DENTIST);
        dentist.setSpecialization("Orthodontist");
        return dentistRepository.save(dentist);
    }

    private Patient createAndSavePatient() {
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setEmail("alice.smith@example.com");
        patient.setPassword("secure456");
        patient.setPhoneNumber("9998887777");
        patient.setRole(RoleEnum.PATIENT);
        patient.setDob(LocalDate.of(1995, 5, 20));
        return patientRepository.save(patient);
    }

    private Address createAndSaveAddress() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Fairfield");
        address.setState("IA");
        address.setZip(52557);
        address.setUnitNo("A1");
        return addressRepository.save(address);
    }

    private Appointment createAndSaveAppointment(Dentist dentist, Patient patient) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("APT-001");
        appointment.setDate(LocalDate.now());
        appointment.setAppointmentTime(LocalTime.of(10, 30));
        appointment.setStatus("SCHEDULED");
        appointment.setDentist(dentist);
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }

    private Surgery createAndSaveSurgery(Address address, Appointment appointment) {
        Surgery surgery = new Surgery();
        surgery.setSurgeryNumber("SUR-001");
        surgery.setSurgeryName("Main Room");
        surgery.setPhone("1234567890");
        surgery.setLocation(address);
        surgery.setAppointment(appointment);
        return surgeryRepository.save(surgery);
    }
}
