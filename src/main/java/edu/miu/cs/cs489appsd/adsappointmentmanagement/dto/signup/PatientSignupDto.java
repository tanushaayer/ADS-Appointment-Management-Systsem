package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record PatientSignupDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phoneNumber,
        @NotBlank @Email String email,
        @NotBlank String password,
        @Past @NotNull LocalDate dob,
        @Valid @NotNull AddressRequestDto address
) {
}
