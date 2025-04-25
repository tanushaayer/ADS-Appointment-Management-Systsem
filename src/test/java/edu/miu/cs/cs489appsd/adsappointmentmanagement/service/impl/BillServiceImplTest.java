package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.BillRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.BillResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.BillMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Bill;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.User;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AppointmentRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.BillRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.PatientRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class BillServiceImplTest {

    @Mock private BillRepository billRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private UserRepository userRepository;
    @Mock private BillMapper billMapper;
    @Mock private Authentication authentication;

    @InjectMocks
    private BillServiceImpl billService;

    private Patient patient;
    private Appointment appointment;
    private Bill bill;
    private BillRequestDto requestDto;
    private BillResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setEmail("patient@example.com");

        appointment = new Appointment();
        appointment.setId(2L);

        bill = new Bill();
        bill.setId(100L);
        bill.setAmount(200.0);
        bill.setPaid(false);
        bill.setAppointment(appointment);
        bill.setPatient(patient);
        bill.setCreatedAt(LocalDateTime.now());

        requestDto = new BillRequestDto(200D, 2L, 1L);
        responseDto = new BillResponseDto(100L, 200.0, false);
    }

    @Test
    @DisplayName("Create bill should return response DTO")
    void createBill_shouldReturnResponseDto() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(2L)).thenReturn(Optional.of(appointment));
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        when(billMapper.toResponse(bill)).thenReturn(responseDto);

        BillResponseDto result = billService.createBill(requestDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findById(2L);
        verify(billRepository).save(any(Bill.class));
        verify(billMapper).toResponse(bill);
    }

    @Test
    @DisplayName("Get all bills should return list of responses")
    void getAllBills_shouldReturnList() {
        when(billRepository.findAll()).thenReturn(List.of(bill));
        when(billMapper.toResponseList(List.of(bill))).thenReturn(List.of(responseDto));

        List<BillResponseDto> result = billService.getAllBills();

        Assertions.assertThat(result).hasSize(1);
        verify(billRepository).findAll();
        verify(billMapper).toResponseList(List.of(bill));
    }

    @Test
    @DisplayName("Get unpaid bills for patient")
    void getUnpaidBillsForPatient_shouldReturnList() {
        when(billRepository.findByPatientIdAndPaidFalse(1L)).thenReturn(List.of(bill));
        when(billMapper.toResponseList(List.of(bill))).thenReturn(List.of(responseDto));

        List<BillResponseDto> result = billService.getUnpaidBillsForPatient(1L);

        Assertions.assertThat(result).hasSize(1);
        verify(billRepository).findByPatientIdAndPaidFalse(1L);
    }

    @Test
    @DisplayName("Mark bill as paid")
    void markAsPaid_shouldUpdateStatus() {
        when(billRepository.findById(100L)).thenReturn(Optional.of(bill));
        billService.markAsPaid(100L);

        Assertions.assertThat(bill.getPaid()).isTrue();
        verify(billRepository).save(bill);
    }

    @Test
    @DisplayName("Get all bills for a patient")
    void getAllBillsForPatient_shouldReturnList() {
        when(billRepository.findByPatientId(1L)).thenReturn(List.of(bill));
        when(billMapper.toResponseList(List.of(bill))).thenReturn(List.of(responseDto));

        List<BillResponseDto> result = billService.getAllBillsForPatient(1L);

        Assertions.assertThat(result).hasSize(1);
        verify(billRepository).findByPatientId(1L);
    }

    @Test
    @DisplayName("Get bills with filters and pagination")
    void getBillsWithFilters_shouldReturnPaginatedResult() {
        when(authentication.getName()).thenReturn("patient@example.com");

        User user = new User();
        user.setEmail("patient@example.com");
        user.setRole(edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum.PATIENT);

        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(user));
        when(billRepository.findAll()).thenReturn(List.of(bill));
        when(billMapper.toResponse(bill)).thenReturn(responseDto);

        Pageable pageable = PageRequest.of(0, 10);

        Page<BillResponseDto> result = billService.getBillsWithFilters(
                authentication, false, null, null, null, null, pageable
        );

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(0);
    }
}
