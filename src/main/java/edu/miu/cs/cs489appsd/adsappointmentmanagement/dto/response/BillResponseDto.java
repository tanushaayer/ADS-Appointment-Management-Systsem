package edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response;

public record BillResponseDto(
        Long id,
        Double amount,
        Boolean paid
) {
}
