package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.OfficeManagerResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.OfficeManagerSignupDto;

public interface OfficeManagerService {
    OfficeManagerResponseDto signupOfficeManager(OfficeManagerSignupDto dto);
}
