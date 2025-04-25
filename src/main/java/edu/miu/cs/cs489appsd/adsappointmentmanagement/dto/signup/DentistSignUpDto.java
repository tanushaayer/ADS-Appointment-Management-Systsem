package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DentistSignUpDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Size(min = 10, max = 12) String phoneNumber,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String password,
        @NotBlank String specialization
) {
}
