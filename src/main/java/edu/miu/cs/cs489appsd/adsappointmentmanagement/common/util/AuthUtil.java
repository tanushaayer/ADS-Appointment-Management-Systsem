package edu.miu.cs.cs489appsd.adsappointmentmanagement.common.util;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.OfficeManager;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.DentistRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.OfficeManagerRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final OfficeManagerRepository officeManagerRepository;

    public Patient getCurrentPatient(Authentication authentication) {
        return patientRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated patient not found."));
    }

    public Dentist getCurrentDentist(Authentication authentication) {
        return dentistRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated dentist not found."));
    }

    public OfficeManager getCurrentOfficeManager(Authentication authentication) {
        return officeManagerRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated office manager not found."));
    }
}
