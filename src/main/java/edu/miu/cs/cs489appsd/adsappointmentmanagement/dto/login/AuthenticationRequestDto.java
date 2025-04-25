package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDto(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
