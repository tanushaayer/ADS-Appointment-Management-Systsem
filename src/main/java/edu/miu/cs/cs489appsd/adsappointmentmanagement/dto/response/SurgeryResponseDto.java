package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response;

import jakarta.validation.constraints.NotNull;

public record SurgeryResponseDto(
        Long id,
        String surgeryNumber,
        String surgeryName,
        String phone,
        AddressResponseDto location
) {
}
