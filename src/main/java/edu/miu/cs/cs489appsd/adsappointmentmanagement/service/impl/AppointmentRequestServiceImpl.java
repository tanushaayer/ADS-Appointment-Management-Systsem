package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestSubmissionDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestApprovalDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentRequestResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.BusinessRuleException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.AppointmentMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.AppointmentRequestMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.*;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.*;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.AppointmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AppointmentRequestServiceImpl implements AppointmentRequestService {

    private final AppointmentRequestMapper mapper;
    private final AppointmentRequestRepository appointmentRequestRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepo;
    private final AppointmentRepository appointmentRepo;
    private final BillRepository billRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentRequestResponseDto submitRequest(AppointmentRequestSubmissionDto dto, Authentication authentication) {
        String email = authentication.getName();
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Check for unpaid bills
        List<Bill> unpaid = billRepository.findByPatientIdAndPaidFalse(patient.getId());
        if (!unpaid.isEmpty()) {
            throw new BusinessRuleException("You have unpaid bills. Please pay before requesting a new appointment.");
        }

        AppointmentRequest request = mapper.toEntity(dto);
        request.setStatus("PENDING");
        request.setPatient(patient);

        return mapper.toResponse(appointmentRequestRepository.save(request));
    }



    @Override
    public List<AppointmentRequestResponseDto> getAllRequests() {
        return mapper.toResponseList(appointmentRequestRepository.findAll());
    }

    @Override
    public AppointmentRequestResponseDto approveRequest(Long requestId, AppointmentRequestApprovalDto approvalDto) {
        AppointmentRequest request = appointmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        Dentist dentist = dentistRepo.findById(approvalDto.dentistId())
                .orElseThrow(() -> new NotFoundException("Dentist not found"));

        LocalDate appointmentDate = approvalDto.appointmentDate();
        LocalTime appointmentTime = approvalDto.appointmentTime();

        // Check dentist's weekly appointment count
        int year = appointmentDate.getYear();
        int week = appointmentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        List<Appointment> weeklyAppointments = appointmentRepo.findByDentistIdAndYearAndWeek(dentist.getId(), year, week);
        if (weeklyAppointments.size() >= 5) {
            throw new BusinessRuleException("DentistNotAvailable: Dentist already has 5 appointments for the week");
        }

        // Check if the dentist is already booked at this date/time
        while (appointmentRepo.isDentistBookedAt(
                dentist.getId(), appointmentDate, appointmentTime)) {
            appointmentTime = appointmentTime.plusMinutes(30); // shift to next available 30-minute slot
        }

        // Create Appointment
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("APT-" + System.currentTimeMillis());
        appointment.setDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus("CONFIRMED");
        appointment.setPatient(request.getPatient());
        appointment.setDentist(dentist);
        appointment.setSurgery(request.getSurgery());
        appointmentRepo.save(appointment);

        request.setStatus("APPROVED");
        request.setDentist(dentist);
        appointmentRequestRepository.save(request);

        AppointmentRequestResponseDto responseDto = new AppointmentRequestResponseDto(
                request.getId(),
                request.getStatus(),
                request.getDate(),
                request.getTime(),
                request.getReason(),
                request.getPatient().getId(),
                appointmentMapper.toAppointmentResponseDto(appointment)
        );
        return responseDto;
    }

    @Override
    public void rejectRequest(Long requestId) {
        AppointmentRequest request = appointmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        request.setStatus("REJECTED");
        appointmentRequestRepository.save(request);
    }
}
