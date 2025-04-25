package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.OfficeManagerResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.OfficeManagerSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.OfficeManagerMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.OfficeManager;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.OfficeManagerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

public class OfficeManagerServiceImplTest {

    @Mock private OfficeManagerRepository managerRepository;
    @Mock private OfficeManagerMapper managerMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OfficeManagerServiceImpl officeManagerService;

    private OfficeManagerSignupDto signupDto;
    private OfficeManager manager;
    private OfficeManagerResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signupDto = new OfficeManagerSignupDto(
                "John",
                "Doe",
                "1234567890",
                "john.doe@example.com",
                "password123"
        );

        manager = new OfficeManager();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setEmail("john.doe@example.com");
        manager.setPassword("encoded-password");
        manager.setPhoneNumber("1234567890");
        manager.setRole(RoleEnum.OFFICE_MANAGER);

        responseDto = new OfficeManagerResponseDto(
                1L,
                "John",
                "Doe",
                "1234567890",
                "john.doe@example.com"
        );
    }

    @Test
    @DisplayName("Signup office manager should encode password, set role, save and return response DTO")
    void signupOfficeManager_shouldReturnResponseDto() {
        // Arrange
        when(managerMapper.toOfficeManager(signupDto)).thenReturn(manager);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(managerRepository.save(manager)).thenReturn(manager);
        when(managerMapper.toOfficeManagerResponseDto(manager)).thenReturn(responseDto);

        // Act
        OfficeManagerResponseDto result = officeManagerService.signupOfficeManager(signupDto);

        // Assert
        Assertions.assertThat(result).isEqualTo(responseDto);

        verify(managerMapper).toOfficeManager(signupDto);
        verify(passwordEncoder).encode("password123");
        verify(managerRepository).save(manager);
        verify(managerMapper).toOfficeManagerResponseDto(manager);
    }
}
