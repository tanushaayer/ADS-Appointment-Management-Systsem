package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.PatientRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.PatientResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.PatientSignupDto;

import java.util.List;

public interface PatientService {
    PatientResponseDto signupPatient(PatientSignupDto dto);
    List<PatientResponseDto> getAllPatients();
    PatientResponseDto getPatientById(Long id);
    PatientResponseDto updatePatient( Long id, PatientRequestDto patientRequestDto);
    void deletePatient(Long id);
    List<PatientResponseDto> searchPatients(String searchString);

}
