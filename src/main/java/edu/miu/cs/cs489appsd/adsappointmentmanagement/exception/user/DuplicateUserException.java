package edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.user;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message){
        super(message);
    }
}
