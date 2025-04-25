package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.BillRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.BillResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.BillMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Bill;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.User;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AppointmentRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.BillRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.PatientRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.UserRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillMapper billMapper;
    private final UserRepository userRepository;

    @Override
    public BillResponseDto createBill(BillRequestDto dto) {
        Patient patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new NotFoundException("Patient not found"));
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        Bill bill = new Bill();
        bill.setAmount(dto.amount());
        bill.setPaid(false);
        bill.setPatient(patient);
        bill.setAppointment(appointment);
        return billMapper.toResponse(billRepository.save(bill));
    }

    @Override
    public List<BillResponseDto> getAllBills() {
        return billMapper.toResponseList(billRepository.findAll());
    }

    @Override
    public List<BillResponseDto> getUnpaidBillsForPatient(Long patientId) {
        return billMapper.toResponseList(billRepository.findByPatientIdAndPaidFalse(patientId));
    }

    @Override
    public void markAsPaid(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new NotFoundException("Bill not found"));
        bill.setPaid(true);
        billRepository.save(bill);
    }


    @Override
    public List<BillResponseDto> getAllBillsForPatient(Long patientId) {
        List<Bill> bills = billRepository.findByPatientId(patientId);
        if (bills.isEmpty()) {
            throw new NotFoundException("No bills found for the patient");
        }
        return billMapper.toResponseList(bills);
    }

    @Override
    public Page<BillResponseDto> getBillsWithFilters(
            Authentication auth,
            Boolean paid,
            Double amountMin,
            Double amountMax,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Bill> filtered = billRepository.findAll().stream()
                .filter(b -> switch (user.getRole()) {
                    case PATIENT -> b.getPatient().getEmail().equals(email);
                    case OFFICE_MANAGER -> true;
                    default -> false;
                })
                .filter(b -> paid == null || b.getPaid())
                .filter(b -> amountMin == null || b.getAmount() >= amountMin)
                .filter(b -> amountMax == null || b.getAmount() <= amountMax)
                .filter(b -> dateFrom == null || !b.getCreatedAt().toLocalDate().isBefore(dateFrom))
                .filter(b -> dateTo == null || !b.getCreatedAt().toLocalDate().isAfter(dateTo))
                .toList();

        List<BillResponseDto> response = filtered.stream()
                .map(billMapper::toResponse)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), response.size());

        return new PageImpl<>(response.subList(start, end), pageable, response.size());
    }

}
