package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.SurgeryRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
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

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAnyRole;

@RestController
@RequestMapping("/api/v1/surgeries")
@RequiredArgsConstructor
public class SurgeryController {

    private final SurgeryService surgeryService;

    // Dentist creates a surgery from appointment
    @PreAuthorize("hasAnyRole('DENTIST', 'OFFICE_MANAGER')")
    @PostMapping
    public ResponseEntity<SurgeryResponseDto> createSurgery(@RequestBody SurgeryRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surgeryService.createFromAppointment(dto));
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DENTIST', 'OFFICE_MANAGER')")
    @GetMapping
    public ResponseEntity<List<SurgeryResponseDto>> getSurgeries(Authentication authentication) {
        return ResponseEntity.ok(surgeryService.getSurgeriesForUser(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurgeryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(surgeryService.getSurgeryById(id));
    }

    @PreAuthorize("hasRole('OFFICE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<SurgeryResponseDto> update(@PathVariable Long id, @Valid @RequestBody SurgeryRequestDto dto) {
        return ResponseEntity.ok(surgeryService.updateSurgery(id, dto));
    }

    @PreAuthorize("hasRole('OFFICE_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        surgeryService.deleteSurgery(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DENTIST', 'OFFICE_MANAGER')")
    @GetMapping("/filter")
    public ResponseEntity<Page<SurgeryResponseDto>> getFilteredSurgeries(
            @RequestParam(required = false) String surgeryName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Long patientId,
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                surgeryService.getSurgeriesWithFilters(authentication, surgeryName, dateFrom, dateTo, patientId, pageable)
        );
    }

}
