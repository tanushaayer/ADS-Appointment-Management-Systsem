package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.OfficeManagerResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.OfficeManagerSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.OfficeManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OfficeManagerMapper {
    OfficeManager toOfficeManager(OfficeManagerSignupDto dto);

    OfficeManagerResponseDto toOfficeManagerResponseDto(OfficeManager officeManager);
}
