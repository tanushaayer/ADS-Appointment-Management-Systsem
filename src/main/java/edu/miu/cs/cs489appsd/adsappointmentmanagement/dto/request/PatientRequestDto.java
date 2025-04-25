package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

import java.time.LocalDate;

public record PatientRequestDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        LocalDate dob,
        AddressRequestDto addressRequestDto
) {
}
