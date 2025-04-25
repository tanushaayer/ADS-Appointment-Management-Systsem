package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.DentistRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.DentistResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.DentistSignUpDto;

import java.util.List;

public interface DentistService {
    DentistResponseDto signupDentist(DentistSignUpDto dto);
    DentistResponseDto createDentist(DentistRequestDto dto);
    List<DentistResponseDto> getAllDentists();
    DentistResponseDto getDentistById(Long id);
    DentistResponseDto updateDentist(Long id, DentistRequestDto dto);
    void deleteDentist(Long id);
}
