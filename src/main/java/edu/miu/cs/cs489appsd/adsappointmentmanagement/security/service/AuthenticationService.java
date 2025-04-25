package edu.miu.cs.cs489appsd.adsappointmentmanagement.security.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login.AuthenticationRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login.AuthenticationResponseDto;

public interface AuthenticationService {
    AuthenticationResponseDto login(AuthenticationRequestDto dto);
}
