package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.util.AuthUtil;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.SurgeryRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Surgery;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.AppointmentService;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.SurgeryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PreAuthorize("hasAnyRole('PATIENT', 'DENTIST', 'OFFICE_MANAGER')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAppointments(Authentication authentication) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForUser(authentication));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getById(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(appointmentService.getAppointmentByIdForUser(id, auth));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('PATIENT','DENTIST','OFFICE_MANAGER')")
    @GetMapping("/filter")
    public ResponseEntity<Page<AppointmentResponseDto>> getFilteredAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Long dentistId,
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsWithFilters(authentication, status, dateFrom, dateTo, dentistId, pageable)
        );
    }
}
