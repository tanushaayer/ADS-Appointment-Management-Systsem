package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DentistRequestDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Size(min = 10, max = 12) String phoneNumber,
        @NotBlank @Email(message = "Email should be valid") String email,
        @NotBlank String password,
        @NotBlank String specialization
) {
}
