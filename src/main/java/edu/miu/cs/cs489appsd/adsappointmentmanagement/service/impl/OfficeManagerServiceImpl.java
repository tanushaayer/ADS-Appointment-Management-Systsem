package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.OfficeManagerResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.OfficeManagerSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.OfficeManagerMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.OfficeManager;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.OfficeManagerRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.OfficeManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfficeManagerServiceImpl implements OfficeManagerService {

    private final OfficeManagerRepository managerRepository;
    private final OfficeManagerMapper managerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OfficeManagerResponseDto signupOfficeManager(OfficeManagerSignupDto dto) {
        OfficeManager manager = managerMapper.toOfficeManager(dto);
        manager.setPassword(passwordEncoder.encode(dto.password()));
        manager.setRole(RoleEnum.OFFICE_MANAGER);
        OfficeManager saved = managerRepository.save(manager);
        return managerMapper.toOfficeManagerResponseDto(saved);
    }
}