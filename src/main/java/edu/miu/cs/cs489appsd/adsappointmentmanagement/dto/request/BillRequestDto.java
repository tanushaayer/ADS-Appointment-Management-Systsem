package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request;

public record BillRequestDto(
        Double amount,
        Long appointmentId,
        Long patientId
) {
}
