package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.DentistRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.DentistResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.DentistSignUpDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.DentistMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.DentistRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DentistServiceImplTest {

    @Mock private DentistRepository dentistRepository;
    @Mock private DentistMapper dentistMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DentistServiceImpl dentistService;

    private Dentist dentist;
    private DentistSignUpDto signUpDto;
    private DentistRequestDto requestDto;
    private DentistResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setFirstName("John");
        dentist.setLastName("Doe");
        dentist.setEmail("john@example.com");
        dentist.setPhoneNumber("1234567890");
        dentist.setSpecialization("Orthodontist");

        signUpDto = new DentistSignUpDto(
                "John",
                "Doe",
                "1234567890",
                "john@example.com",
                "password123",
                "Orthodontist"
        );

        requestDto = new DentistRequestDto(
                "John",
                "Doe",
                "123455632",
                "john@example.com",
                "1234567890",
                "Orthodontist"
        );

        responseDto = new DentistResponseDto(
                1L,
                "John",
                "Doe",
                "1234567890",
                "john@example.com",
                "Orthodontist"
        );
    }

    @Test
    @DisplayName("Signup dentist should encode password, set role and save")
    void signupDentist_shouldReturnResponseDto() {
        when(dentistMapper.toDentist(signUpDto)).thenReturn(dentist);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(dentistRepository.save(any(Dentist.class))).thenReturn(dentist);
        when(dentistMapper.toDentistResponseDto(dentist)).thenReturn(responseDto);

        DentistResponseDto result = dentistService.signupDentist(signUpDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(dentistMapper).toDentist(signUpDto);
        verify(passwordEncoder).encode("password123");
        verify(dentistRepository).save(dentist);
        verify(dentistMapper).toDentistResponseDto(dentist);
    }

    @Test
    @DisplayName("Create dentist should save and return")
    void createDentist_shouldReturnResponseDto() {
        when(dentistMapper.toDentist(requestDto)).thenReturn(dentist);
        when(dentistRepository.save(dentist)).thenReturn(dentist);
        when(dentistMapper.toDentistResponseDto(dentist)).thenReturn(responseDto);

        DentistResponseDto result = dentistService.createDentist(requestDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(dentistMapper).toDentist(requestDto);
        verify(dentistRepository).save(dentist);
        verify(dentistMapper).toDentistResponseDto(dentist);
    }

    @Test
    @DisplayName("Get all dentists should return list")
    void getAllDentists_shouldReturnList() {
        when(dentistRepository.findAll()).thenReturn(List.of(dentist));
        when(dentistMapper.toDentistResponseDtoList(List.of(dentist))).thenReturn(List.of(responseDto));

        List<DentistResponseDto> result = dentistService.getAllDentists();

        Assertions.assertThat(result).hasSize(1);
        verify(dentistRepository).findAll();
        verify(dentistMapper).toDentistResponseDtoList(List.of(dentist));
    }

    @Test
    @DisplayName("Get dentist by id should return dto")
    void getDentistById_shouldReturnDto() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        when(dentistMapper.toDentistResponseDto(dentist)).thenReturn(responseDto);

        DentistResponseDto result = dentistService.getDentistById(1L);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(dentistRepository).findById(1L);
        verify(dentistMapper).toDentistResponseDto(dentist);
    }

    @Test
    @DisplayName("Get dentist by id should throw NotFoundException if not found")
    void getDentistById_notFound_shouldThrow() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> dentistService.getDentistById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Dentist not found with id 1");

        verify(dentistRepository).findById(1L);
    }

    @Test
    @DisplayName("Update dentist should modify fields and save")
    void updateDentist_shouldReturnUpdatedDto() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        when(dentistRepository.save(dentist)).thenReturn(dentist);
        when(dentistMapper.toDentistResponseDto(dentist)).thenReturn(responseDto);

        DentistResponseDto result = dentistService.updateDentist(1L, requestDto);

        Assertions.assertThat(result).isEqualTo(responseDto);
        verify(dentistRepository).findById(1L);
        verify(dentistRepository).save(dentist);
        verify(dentistMapper).toDentistResponseDto(dentist);
    }

    @Test
    @DisplayName("Delete dentist should remove entity")
    void deleteDentist_shouldDeleteSuccessfully() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));

        dentistService.deleteDentist(1L);

        verify(dentistRepository).findById(1L);
        verify(dentistRepository).delete(dentist);
    }

    @Test
    @DisplayName("Delete dentist should throw NotFoundException if not found")
    void deleteDentist_notFound_shouldThrow() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> dentistService.deleteDentist(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Dentist not found with id 1");

        verify(dentistRepository).findById(1L);
    }
}
