package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.DentistRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.DentistResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.DentistSignUpDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.DentistMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.DentistRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.DentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {

    private final DentistRepository dentistRepository;
    private final DentistMapper dentistMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public DentistResponseDto signupDentist(DentistSignUpDto dto) {
        Dentist dentist = dentistMapper.toDentist(dto);
        dentist.setRole(RoleEnum.DENTIST);
        dentist.setPassword(passwordEncoder.encode(dto.password()));
        return dentistMapper.toDentistResponseDto(dentistRepository.save(dentist));
    }


    @Override
    public DentistResponseDto createDentist(DentistRequestDto dto) {
        Dentist saved = dentistRepository.save(dentistMapper.toDentist(dto));
        return dentistMapper.toDentistResponseDto(saved);
    }

    @Override
    public List<DentistResponseDto> getAllDentists() {
        return dentistMapper.toDentistResponseDtoList(dentistRepository.findAll());
    }

    @Override
    public DentistResponseDto getDentistById(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist not found with id " + id));
        return dentistMapper.toDentistResponseDto(dentist);
    }

    @Override
    public DentistResponseDto updateDentist(Long id, DentistRequestDto dto) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist not found with id " + id));
        dentist.setFirstName(dto.firstName());
        dentist.setLastName(dto.lastName());
        dentist.setEmail(dto.email());
        dentist.setPhoneNumber(dto.phoneNumber());
        dentist.setSpecialization(dto.specialization());
        return dentistMapper.toDentistResponseDto(dentistRepository.save(dentist));
    }

    @Override
    public void deleteDentist(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dentist not found with id " + id));
        dentistRepository.delete(dentist);
    }
}
