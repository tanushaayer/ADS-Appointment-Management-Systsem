package edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
