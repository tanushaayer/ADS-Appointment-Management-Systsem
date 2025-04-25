package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestSubmissionDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestApprovalDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentRequestResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.AppointmentRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment-requests")
@RequiredArgsConstructor
public class AppointmentRequestController {

    private final AppointmentRequestService service;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentRequestResponseDto> submitRequest(
            @Valid @RequestBody AppointmentRequestSubmissionDto dto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.submitRequest(dto, authentication));
    }


    @PreAuthorize("hasRole('OFFICE_MANAGER')")
    @GetMapping
    public ResponseEntity<List<AppointmentRequestResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @PreAuthorize("hasRole('OFFICE_MANAGER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<AppointmentRequestResponseDto> approve(@PathVariable Long id, @Valid @RequestBody AppointmentRequestApprovalDto dto) {
        AppointmentRequestResponseDto appointmentRequestResponseDto = service.approveRequest(id, dto);
        return ResponseEntity.accepted().body(appointmentRequestResponseDto);
    }

    @PreAuthorize("hasRole('OFFICE_MANAGER')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        service.rejectRequest(id);
        return ResponseEntity.noContent().build();
    }
}
