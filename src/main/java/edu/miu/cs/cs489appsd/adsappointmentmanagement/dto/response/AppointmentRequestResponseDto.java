package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestResponseDto(
        Long id,
        String status,
        LocalDate date,
        LocalTime time,
        String reason,
        Long patientId,
        AppointmentResponseDto appointment
) {
}
