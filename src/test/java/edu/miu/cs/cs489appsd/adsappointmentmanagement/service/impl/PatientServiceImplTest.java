
package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.PatientRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AddressResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.PatientResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.PatientSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.AddressMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.PatientMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AddressRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.PatientRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.PatientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class PatientServiceImplTest {

    @Mock private PatientRepository patientRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private PatientMapper patientMapper;
    @Mock private AddressMapper addressMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientSignupDto signupDto;
    private PatientResponseDto responseDto;
    private AddressResponseDto addressResponseDto;
    private AddressRequestDto addressRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        addressRequestDto = new AddressRequestDto(
                "1a", "1000 N 4th St", "Fairfield", "IA", 52557
        );

        Address address = new Address();
        address.setStreet("1000 N 4th St");
        address.setCity("Fairfield");
        address.setState("IA");
        address.setZip(52557);
        address.setUnitNo("1a");

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john@example.com");
        patient.setDob(LocalDate.of(1990, 1, 1));
        patient.setAddress(address);

        signupDto = new PatientSignupDto(
                "John", "Doe", "123456789", "john@example.com", "password",
                LocalDate.of(1990, 1, 1), addressRequestDto
        );

        addressResponseDto = new AddressResponseDto(
                "1a", "1000 N 4th St", "Fairfield", "IA", 52557
        );

        responseDto = new PatientResponseDto(
                "John", "Doe", "123456789", addressResponseDto, null
        );
    }

    @Test
    void signupPatient_shouldReturnResponseDto() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(addressMapper.addressRequestDtoToAddress(addressRequestDto)).thenReturn(patient.getAddress());
        when(patientMapper.patientSignupToPatient(signupDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        PatientResponseDto result = patientService.signupPatient(signupDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(patientRepository).save(patient);
    }

    @Test
    void getAllPatients_ShouldReturnList() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        List<PatientResponseDto> result = patientService.getAllPatients();

        Assertions.assertThat(result).hasSize(0);
        verify(patientRepository).findAll();
    }

    @Test
    void getPatientById_ShouldReturnDto() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        PatientResponseDto result = patientService.getPatientById(1L);

        Assertions.assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void updatePatient_ShouldReturnUpdatedDto() {
        PatientRequestDto requestDto = new PatientRequestDto(
                "Lan", "Huwang", "12345672", "lan@gmail.com",
                LocalDate.of(1998,2,10), addressRequestDto
        );
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.patientRequestToPatient(requestDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.patientToPatientResponseDto(patient)).thenReturn(responseDto);

        PatientResponseDto result = patientService.updatePatient(1L, requestDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(patientRepository).save(patient);
    }
}
