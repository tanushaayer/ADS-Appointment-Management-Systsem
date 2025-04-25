package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.DentistRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.DentistResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.DentistSignUpDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Dentist;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DentistMapper {
    Dentist toDentist(DentistSignUpDto dto);
    Dentist toDentist(DentistRequestDto dto);

    DentistResponseDto toDentistResponseDto(Dentist dentist);
    List<DentistResponseDto> toDentistResponseDtoList(List<Dentist> dentists);
}
