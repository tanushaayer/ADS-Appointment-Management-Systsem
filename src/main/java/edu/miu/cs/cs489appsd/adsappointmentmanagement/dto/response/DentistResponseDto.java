package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response;

public record DentistResponseDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String specialization
) {
}
