package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SurgeryRequestDto(
        @NotBlank String surgeryNumber,
        @NotBlank String surgeryName,
        @NotBlank String phone,
        @NotNull AddressRequestDto location,
        @NotNull Long appointmentId,
        @NotNull @Positive Double surgeryPrice
) {
}
