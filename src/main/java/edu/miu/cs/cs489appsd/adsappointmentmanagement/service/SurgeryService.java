package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.SurgeryRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

public interface SurgeryService {
    List<SurgeryResponseDto> getSurgeriesForUser(Authentication authentication);
    SurgeryResponseDto getSurgeryById(Long id);
    SurgeryResponseDto updateSurgery(Long id, SurgeryRequestDto dto);
    void deleteSurgery(Long id);
    SurgeryResponseDto createFromAppointment(SurgeryRequestDto dto);
    Page<SurgeryResponseDto> getSurgeriesWithFilters(
            Authentication auth,
            String surgeryName,
            LocalDate dateFrom,
            LocalDate dateTo,
            Long patientId,
            Pageable pageable);
}
