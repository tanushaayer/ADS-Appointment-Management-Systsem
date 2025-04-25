package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response;

import java.util.List;

public record PatientResponseDto(
        String firstName,
        String lastName,
        String phoneNumber,
        AddressResponseDto addressResponseDto,
        List<AppointmentResponseDto> appointmentResponseDtos
) {
}
