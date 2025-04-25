package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.SurgeryRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.user.AccessDeniedException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.SurgeryMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.*;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AppointmentRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.BillRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.SurgeryRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.UserRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.SurgeryService;
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
public class SurgeryServiceImpl implements SurgeryService {

    private final SurgeryRepository surgeryRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final SurgeryMapper surgeryMapper;
    private final UserRepository userRepository;

    @Override
    public List<SurgeryResponseDto> getSurgeriesForUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Surgery> surgeries;

        switch (user.getRole()) {
            case PATIENT -> surgeries = surgeryRepository.findByPatientEmail(email);
            case DENTIST -> surgeries = surgeryRepository.findByDentistEmail(email);
            case OFFICE_MANAGER -> surgeries = surgeryRepository.findAll();
            default -> throw new AccessDeniedException("User role not authorized");
        }

        return surgeries.stream()
                .map(surgeryMapper::toSurgeryResponseDto)
                .toList();
    }


    @Override
    public SurgeryResponseDto getSurgeryById(Long id) {
        return surgeryRepository.findById(id)
                .map(surgeryMapper::toSurgeryResponseDto)
                .orElseThrow(() -> new NotFoundException("Surgery not found with id " + id));
    }

    @Override
    public SurgeryResponseDto updateSurgery(Long id, SurgeryRequestDto dto) {
        Surgery surgery = surgeryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Surgery not found with id " + id));
        surgery.setSurgeryName(dto.surgeryName());
        surgery.setSurgeryNumber(dto.surgeryNumber());
        surgery.setPhone(dto.phone());
        surgery.setLocation(surgeryMapper.toSurgery(dto).getLocation());
        return surgeryMapper.toSurgeryResponseDto(surgeryRepository.save(surgery));
    }

    @Override
    public void deleteSurgery(Long id) {
        if (!surgeryRepository.existsById(id)) {
            throw new NotFoundException("Surgery not found with id " + id);
        }
        surgeryRepository.deleteById(id);
    }

    @Override
    public SurgeryResponseDto createFromAppointment(SurgeryRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        Surgery surgery = new Surgery();
        surgery.setSurgeryNumber(dto.surgeryNumber());
        surgery.setSurgeryName(dto.surgeryName());
        surgery.setPhone(dto.phone());

        Address address = new Address();
        address.setStreet(dto.location().street());
        address.setCity(dto.location().city());
        address.setState(dto.location().state());
        address.setZip(dto.location().zip());
        surgery.setLocation(address); // cascade will handle saving

        surgery.setAppointment(appointment);
        Surgery savedSurgery = surgeryRepository.save(surgery);

        Bill bill = new Bill();
        bill.setAmount(dto.surgeryPrice());
        bill.setPaid(false);
        bill.setAppointment(appointment);
        bill.setPatient(appointment.getPatient());
        billRepository.save(bill);

        return surgeryMapper.toSurgeryResponseDto(savedSurgery);
    }

    @Override
    public Page<SurgeryResponseDto> getSurgeriesWithFilters(
            Authentication auth,
            String surgeryName,
            LocalDate dateFrom,
            LocalDate dateTo,
            Long patientId,
            Pageable pageable) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Surgery> filtered = surgeryRepository.findAll().stream()
                .filter(s -> switch (user.getRole()) {
                    case PATIENT -> s.getAppointment().getPatient().getEmail().equals(email);
                    case DENTIST -> s.getAppointment().getDentist().getEmail().equals(email);
                    case OFFICE_MANAGER -> true;
                    default -> false;
                })
                .filter(s -> surgeryName == null || s.getSurgeryName().toLowerCase().contains(surgeryName.toLowerCase()))
                .filter(s -> dateFrom == null || !s.getAppointment().getDate().isBefore(dateFrom))
                .filter(s -> dateTo == null || !s.getAppointment().getDate().isAfter(dateTo))
                .filter(s -> patientId == null || s.getAppointment().getPatient().getId().equals(patientId))
                .toList();

        List<SurgeryResponseDto> response = filtered.stream()
                .map(surgeryMapper::toSurgeryResponseDto)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), response.size());

        return new PageImpl<>(response.subList(start, end), pageable, response.size());
    }



}
