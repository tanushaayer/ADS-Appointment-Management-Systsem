package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestSubmissionDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentRequestResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.AppointmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "surgery", ignore = true)
    @Mapping(target = "dentist", ignore = true)
    AppointmentRequest toEntity(AppointmentRequestSubmissionDto dto);

    AppointmentRequestResponseDto toResponse(AppointmentRequest entity);

    List<AppointmentRequestResponseDto> toResponseList(List<AppointmentRequest> entities);
}
