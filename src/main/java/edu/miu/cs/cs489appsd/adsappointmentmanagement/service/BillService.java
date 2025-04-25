package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.BillRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.BillResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

public interface BillService {
    BillResponseDto createBill(BillRequestDto dto);
    List<BillResponseDto> getAllBills();
    List<BillResponseDto> getUnpaidBillsForPatient(Long patientId);
    void markAsPaid(Long billId);
    List<BillResponseDto> getAllBillsForPatient(Long patientId);
    Page<BillResponseDto> getBillsWithFilters(
            Authentication auth,
            Boolean paid,
            Double amountMin,
            Double amountMax,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable);
}

