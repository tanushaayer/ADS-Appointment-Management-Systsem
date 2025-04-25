package edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.user;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
