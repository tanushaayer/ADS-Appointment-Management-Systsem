package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponseDto(
        String appointmentNumber,
        LocalDate date,
        LocalTime appointmentTime,
        String status
) {
}
