package edu.miu.cs.cs489appsd.adsappointmentmanagement.security.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login.AuthenticationRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login.AuthenticationResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.security.CustomUserDetails;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.security.service.AuthenticationService;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        return new AuthenticationResponseDto(jwt);
    }
}