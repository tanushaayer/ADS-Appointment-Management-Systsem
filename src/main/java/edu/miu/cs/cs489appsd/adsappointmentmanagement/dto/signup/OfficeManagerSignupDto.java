package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OfficeManagerSignupDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber,
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
