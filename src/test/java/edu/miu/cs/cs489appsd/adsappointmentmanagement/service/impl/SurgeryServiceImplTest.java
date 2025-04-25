package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.SurgeryRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AddressResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.SurgeryMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.*;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SurgeryServiceImplTest {

    @Mock private SurgeryRepository surgeryRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private BillRepository billRepository;
    @Mock private UserRepository userRepository;
    @Mock private SurgeryMapper surgeryMapper;
    @Mock private Authentication authentication;

    @InjectMocks
    private SurgeryServiceImpl surgeryService;

    private Surgery surgery;
    private Appointment appointment;
    private Patient patient;
    private SurgeryRequestDto surgeryRequestDto;
    private SurgeryResponseDto surgeryResponseDto;
    private AddressRequestDto addressRequestDto;
    private AddressResponseDto addressResponseDto;
    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        addressRequestDto = new AddressRequestDto(
                "A1", "1000 N 4th St", "Fairfield", "IA", 52557
        );
        addressResponseDto = new AddressResponseDto(
                        "A1", "1000 N 4th St", "Fairfield", "IA", 52557
        );

        address = new Address();
        address.setStreet("1000 N 4th St");
        address.setCity("Fairfield");
        address.setState("IA");
        address.setZip(52557);
        address.setUnitNo("A1");

        patient = new Patient();
        patient.setId(1L);
        patient.setEmail("patient@example.com");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now());
        appointment.setPatient(patient);
        appointment.setDentist(new Dentist());

        surgery = new Surgery();
        surgery.setId(1L);
        surgery.setSurgeryNumber("SUR-001");
        surgery.setSurgeryName("Main Surgery");
        surgery.setPhone("1234567890");
        surgery.setLocation(address);
        surgery.setAppointment(appointment);

        surgeryRequestDto = new SurgeryRequestDto(
                "SUR-001", "Main Surgery", "1234567890", addressRequestDto, 1L, 500.0
        );

        surgeryResponseDto = new SurgeryResponseDto(
                1L,"SUR-001", "Main Surgery", "1234567890", addressResponseDto
        );

        user = new User();
        user.setEmail("patient@example.com");
        user.setRole(RoleEnum.PATIENT);
    }

    @Test
    @DisplayName("Get surgeries for authenticated user (PATIENT)")
    void getSurgeriesForUser_shouldReturnList() {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(surgeryRepository.findByPatientEmail(user.getEmail())).thenReturn(List.of(surgery));
        when(surgeryMapper.toSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        List<SurgeryResponseDto> result = surgeryService.getSurgeriesForUser(authentication);

        Assertions.assertThat(result).hasSize(1);
        verify(userRepository).findByEmail(user.getEmail());
        verify(surgeryRepository).findByPatientEmail(user.getEmail());
    }

    @Test
    @DisplayName("Get surgery by id should return dto")
    void getSurgeryById_shouldReturnDto() {
        when(surgeryRepository.findById(1L)).thenReturn(Optional.of(surgery));
        when(surgeryMapper.toSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        SurgeryResponseDto result = surgeryService.getSurgeryById(1L);

        Assertions.assertThat(result).isEqualTo(surgeryResponseDto);
        verify(surgeryRepository).findById(1L);
    }

    @Test
    @DisplayName("Get surgery by id should throw if not found")
    void getSurgeryById_notFound_shouldThrow() {
        when(surgeryRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> surgeryService.getSurgeryById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Surgery not found with id 1");
    }

    @Test
    @DisplayName("Update surgery should save updated fields")
    void updateSurgery_shouldReturnUpdatedDto() {
        when(surgeryRepository.findById(1L)).thenReturn(Optional.of(surgery));
        when(surgeryRepository.save(surgery)).thenReturn(surgery);
        when(surgeryMapper.toSurgery(surgeryRequestDto)).thenReturn(surgery);
        when(surgeryMapper.toSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        SurgeryResponseDto result = surgeryService.updateSurgery(1L, surgeryRequestDto);

        Assertions.assertThat(result).isEqualTo(surgeryResponseDto);
        verify(surgeryRepository).save(surgery);
    }

    @Test
    @DisplayName("Delete surgery should remove entity")
    void deleteSurgery_shouldDeleteSuccessfully() {
        when(surgeryRepository.existsById(1L)).thenReturn(true);

        surgeryService.deleteSurgery(1L);

        verify(surgeryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete surgery should throw if not found")
    void deleteSurgery_notFound_shouldThrow() {
        when(surgeryRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> surgeryService.deleteSurgery(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Surgery not found with id 1");
    }

    @Test
    @DisplayName("Create surgery from appointment should save surgery and bill")
    void createFromAppointment_shouldCreateSurgeryAndBill() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(surgeryRepository.save(any(Surgery.class))).thenReturn(surgery);
        when(surgeryMapper.toSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        SurgeryResponseDto result = surgeryService.createFromAppointment(surgeryRequestDto);

        Assertions.assertThat(result).isEqualTo(surgeryResponseDto);
        verify(appointmentRepository).findById(1L);
        verify(billRepository).save(any(Bill.class));
    }

    @Test
    @DisplayName("Get surgeries with filters for PATIENT role")
    void getSurgeriesWithFilters_shouldReturnPaginatedList() {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(surgeryRepository.findAll()).thenReturn(List.of(surgery));
        when(surgeryMapper.toSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        Pageable pageable = PageRequest.of(0, 10);
        Page<SurgeryResponseDto> result = surgeryService.getSurgeriesWithFilters(
                authentication, null, null, null, null, pageable
        );

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
    }
}
