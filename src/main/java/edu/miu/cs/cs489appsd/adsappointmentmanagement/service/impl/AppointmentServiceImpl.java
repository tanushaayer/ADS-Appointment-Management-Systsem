package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.BusinessRuleException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.user.AccessDeniedException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.AppointmentMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.User;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AppointmentRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.DentistRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.UserRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
    private final DentistRepository dentistRepository;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto dto) {
        Dentist dentist = dentistRepository.findById(dto.doctorId())
                .orElseThrow(() -> new BusinessRuleException("Dentist not found"));

        LocalDate date = dto.date();
        int year, week;
        List<Appointment> weeklyAppointments;

        do {
            year = date.getYear();
            week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
            weeklyAppointments = appointmentRepository.findByDentistIdAndYearAndWeek(dentist.getId(), year, week);

            if (weeklyAppointments.size() >= 5) {
                date = date.plusWeeks(1); // Shift to next week
            }
        } while (weeklyAppointments.size() >= 5);

        Appointment appointment = appointmentMapper.toAppointment(dto);
        appointment.setDate(date); // Updated, if needed
        appointment.setDentist(dentist);
        Appointment saved = appointmentRepository.save(appointment);

        return appointmentMapper.toAppointmentResponseDto(saved);
    }
    @Override
    public List<AppointmentResponseDto> getAppointmentsForUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Appointment> appointments;

        switch (user.getRole()) {
            case PATIENT -> appointments = appointmentRepository.findByPatientEmail(email);
            case DENTIST -> appointments = appointmentRepository.findByDentistEmail(email);
            case OFFICE_MANAGER -> appointments = appointmentRepository.findAll();
            default -> throw new AccessDeniedException("User role not authorized");
        }

        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponseDto)
                .toList();
    }


    @Override
    public AppointmentResponseDto getAppointmentByIdForUser(Long id, Authentication auth) {
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        boolean isPatient = user.getRole() == RoleEnum.PATIENT &&
                appointment.getPatient().getEmail().equals(email);

        boolean isDentist = user.getRole() == RoleEnum.DENTIST &&
                appointment.getDentist().getEmail().equals(email);

        boolean isManager = user.getRole() == RoleEnum.OFFICE_MANAGER;

        if (!(isPatient || isDentist || isManager)) {
            throw new AccessDeniedException("You are not authorized to view this appointment.");
        }

        return appointmentMapper.toAppointmentResponseDto(appointment);
    }

    @Override
    public Page<AppointmentResponseDto> getAppointmentsWithFilters(
            Authentication auth,
            String status,
            LocalDate dateFrom,
            LocalDate dateTo,
            Long dentistId,
            Pageable pageable) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        RoleEnum role = user.getRole();

        // Fetch all filtered appointments in-memory (replace later with JPA specs)
        List<Appointment> filtered = appointmentRepository.findAll().stream()
                .filter(a -> switch (role) {
                    case PATIENT -> a.getPatient().getEmail().equals(email);
                    case DENTIST -> a.getDentist().getEmail().equals(email);
                    case OFFICE_MANAGER -> true;
                    default -> false;
                })
                .filter(a -> status == null || a.getStatus().equalsIgnoreCase(status))
                .filter(a -> dateFrom == null || !a.getDate().isBefore(dateFrom))
                .filter(a -> dateTo == null || !a.getDate().isAfter(dateTo))
                .filter(a -> dentistId == null || a.getDentist().getId().equals(dentistId))
                .toList();

        List<AppointmentResponseDto> response = filtered.stream()
                .map(appointmentMapper::toAppointmentResponseDto)
                .toList();

        // Manually build a Page (simulate pagination)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), response.size());
        List<AppointmentResponseDto> paged = response.subList(start, end);

        return new PageImpl<>(paged, pageable, response.size());
    }


    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentResponseDto> findByDentist(Dentist dentist) {
        List<Appointment> appointments = appointmentRepository.findByDentist(dentist);
        return appointmentMapper.toAppointmentResponseDtoList(appointments);
    }

}
