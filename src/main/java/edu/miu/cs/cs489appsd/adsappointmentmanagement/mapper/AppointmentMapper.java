package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dentist", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "surgery", ignore = true)
    Appointment toAppointment(AppointmentRequestDto dto);

    AppointmentResponseDto toAppointmentResponseDto(Appointment appointment);

    List<AppointmentResponseDto> toAppointmentResponseDtoList(List<Appointment> appointments);
}

