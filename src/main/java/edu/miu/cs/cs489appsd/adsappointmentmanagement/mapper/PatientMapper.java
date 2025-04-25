package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.PatientRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.PatientResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.PatientSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientMapper {

    // Patient creation from update request
    @Mapping(source = "addressRequestDto", target = "address")
    Patient patientRequestToPatient(PatientRequestDto patientRequestDto);

    // Patient creation from signup request
    @Mapping(source = "address", target = "address")
    Patient patientSignupToPatient(PatientSignupDto dto);

    // Patient to response
    @Mappings({
            @Mapping(source = "address", target = "addressResponseDto"),
            @Mapping(source = "appointments", target = "appointmentResponseDtos")
    })
    PatientResponseDto patientToPatientResponseDto(Patient patient);

    // List mapping
    @Mappings({
            @Mapping(source = "address", target = "addressResponseDto"),
            @Mapping(source = "appointments", target = "appointmentResponseDtos")
    })
    List<PatientResponseDto> patientListToPatientResponseDtoList(List<Patient> patients);

    // Address helper mapping (if not already in a separate AddressMapper)
    Address addressRequestDtoToAddress(AddressRequestDto dto);
}
