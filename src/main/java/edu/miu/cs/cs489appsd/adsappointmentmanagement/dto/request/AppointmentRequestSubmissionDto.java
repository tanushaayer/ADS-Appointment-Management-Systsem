package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestSubmissionDto(
        @Future @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotBlank String reason
) {
}
