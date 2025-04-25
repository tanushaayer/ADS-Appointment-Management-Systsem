package edu.miu.cs.cs489appsd.adsappointmentmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dentists")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Dentist extends User {

    @Column(nullable = false, length = 32)
    private String specialization;

    @OneToMany(mappedBy = "dentist")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "dentist")
    private List<AppointmentRequest> appointmentRequests = new ArrayList<>();

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

}