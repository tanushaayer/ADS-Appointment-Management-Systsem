package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.AppointmentMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.*;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AppointmentRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.DentistRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.PatientRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.*;

public class AppointmentServiceImplTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private DentistRepository dentistRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private UserRepository userRepository;
    @Mock private AppointmentMapper appointmentMapper;
    @Mock private org.springframework.security.core.Authentication authentication;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment appointment;
    private AppointmentRequestDto requestDto;
    private AppointmentResponseDto responseDto;
    private Dentist dentist;
    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setEmail("dentist@example.com");

        patient = new Patient();
        patient.setId(1L);
        patient.setEmail("john.doe@example.com");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentNumber("APT-001");
        appointment.setDate(LocalDate.now());
        appointment.setAppointmentTime(LocalTime.of(10, 0));
        appointment.setStatus("SCHEDULED");
        appointment.setDentist(dentist);
        appointment.setPatient(patient);

        requestDto = new AppointmentRequestDto(
                "APT-001", appointment.getDate(), appointment.getAppointmentTime(),
                appointment.getStatus(), dentist.getId(), patient.getId()
        );

        responseDto = new AppointmentResponseDto(
                appointment.getAppointmentNumber(), appointment.getDate(),
                appointment.getAppointmentTime(), appointment.getStatus()
        );
    }

    @Test
    @DisplayName("Create appointment should return response DTO")
    void createAppointment_ShouldReturnDto() {
        when(dentistRepository.findById(dentist.getId())).thenReturn(Optional.of(dentist));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentMapper.toAppointment(requestDto)).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.toAppointmentResponseDto(appointment)).thenReturn(responseDto);

        AppointmentResponseDto result = appointmentService.createAppointment(requestDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
    }


    @Test
    @DisplayName("Get appointment by ID for user should succeed")
    void getAppointmentByIdForUser_ShouldReturnDto() {
        when(authentication.getName()).thenReturn(patient.getEmail());
        User user = new User();
        user.setEmail(patient.getEmail());
        user.setRole(RoleEnum.PATIENT);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toAppointmentResponseDto(appointment)).thenReturn(responseDto);

        var result = appointmentService.getAppointmentByIdForUser(appointment.getId(), authentication);
        Assertions.assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Cancel appointment by ID should set status to CANCELLED")
    void cancelAppointment_ShouldUpdateStatus() {
        when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(appointment.getId());

        Assertions.assertThat(appointment.getStatus()).isEqualTo("CANCELLED");
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("Get filtered & paginated appointments for PATIENT")
    void getAppointmentsWithFilters_ShouldReturnPage() {
        when(authentication.getName()).thenReturn(patient.getEmail());

        User user = new User();
        user.setEmail(patient.getEmail());
        user.setRole(RoleEnum.PATIENT);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        appointment.setPatient(patient);
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentMapper.toAppointmentResponseDto(appointment)).thenReturn(responseDto);

        Pageable pageable = PageRequest.of(0, 5);

        var result = appointmentService.getAppointmentsWithFilters(
                authentication, "SCHEDULED", null, null, null, pageable
        );

        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
