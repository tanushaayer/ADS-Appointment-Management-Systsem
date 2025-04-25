package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(AppointmentRequestDto dto);
    List<AppointmentResponseDto> getAppointmentsForUser(Authentication authentication);
    AppointmentResponseDto getAppointmentByIdForUser(Long id, Authentication auth);
    void cancelAppointment(Long id);
    List<AppointmentResponseDto> findByDentist(Dentist dentist);
    Page<AppointmentResponseDto> getAppointmentsWithFilters(
            Authentication auth,
            String status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Long dentistId,
            Pageable pageable);
}
