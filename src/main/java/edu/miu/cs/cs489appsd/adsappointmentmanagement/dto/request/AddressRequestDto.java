package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDto(
        @NotBlank String unitNo,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String state,
        @NotNull Integer zip
) {
}
