package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestApprovalDto(
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        Long dentistId
) {
}
