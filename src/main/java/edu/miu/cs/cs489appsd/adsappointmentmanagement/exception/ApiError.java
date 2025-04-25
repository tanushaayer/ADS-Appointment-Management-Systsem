package edu.miu.cs.cs489appsd.adsappointmentmanagement.exception;

import java.time.Instant;

public record ApiError(
        String message,
        String path,
        Integer statusCode,
        Instant timestamp
) {
}
