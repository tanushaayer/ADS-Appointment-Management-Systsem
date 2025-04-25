package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.util.AuthUtil;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.BillRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.BillResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.BillService;
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
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;
    private final AuthUtil authUtil;

    @PreAuthorize("hasRole('OFFICE_MANAGER')")
    @PostMapping
    public ResponseEntity<BillResponseDto> create(@Valid @RequestBody BillRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.createBill(dto));
    }

    @GetMapping
    public ResponseEntity<List<BillResponseDto>> getAll() {
        return ResponseEntity.ok(billService.getAllBills());
    }
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/bills")
    public ResponseEntity<List<BillResponseDto>> getAllBillsForPatient(Authentication auth) {
        Patient patient = authUtil.getCurrentPatient(auth);
        return ResponseEntity.ok(billService.getAllBillsForPatient(patient.getId()));
    }


    @PreAuthorize("hasAnyRole('PATIENT', 'OFFICE_MANAGER')")
    @GetMapping("/unpaid/{patientId}")
    public ResponseEntity<List<BillResponseDto>> getUnpaid(@PathVariable Long patientId) {
        return ResponseEntity.ok(billService.getUnpaidBillsForPatient(patientId));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/{id}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        billService.markAsPaid(id);
        return ResponseEntity.noContent().build();
    }
}
