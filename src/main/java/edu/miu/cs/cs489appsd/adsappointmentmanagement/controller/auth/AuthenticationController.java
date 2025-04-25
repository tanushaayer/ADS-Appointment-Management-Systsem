package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller.auth;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login.AuthenticationRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login.AuthenticationResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.DentistResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.OfficeManagerResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.PatientResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.DentistSignUpDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.OfficeManagerSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.PatientSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.security.service.AuthenticationService;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.DentistService;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.OfficeManagerService;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final PatientService patientService;
    private final OfficeManagerService officeManagerService;
    private final DentistService dentistService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup/patient")
    public ResponseEntity<PatientResponseDto> signupPatient(@Valid @RequestBody PatientSignupDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.signupPatient(dto));
    }

    @PostMapping("/signup/office-manager")
    public ResponseEntity<OfficeManagerResponseDto> signupManager(@Valid @RequestBody OfficeManagerSignupDto dto) {
        OfficeManagerResponseDto officeManager = officeManagerService.signupOfficeManager(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(officeManager);
    }

    @PostMapping("/signup/dentist")
    public ResponseEntity<DentistResponseDto> signupDentist(@Valid @RequestBody DentistSignUpDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dentistService.signupDentist(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto dto) {
        return ResponseEntity.ok(authenticationService.login(dto));
    }

}
