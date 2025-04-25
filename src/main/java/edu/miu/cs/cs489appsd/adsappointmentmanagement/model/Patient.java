package edu.miu.cs.cs489appsd.adsappointmentmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Patient extends User {

    @Column(nullable = false)
    private LocalDate dob;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<AppointmentRequest> appointmentRequests = new ArrayList<>();

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void addAppointmentRequest(AppointmentRequest request) {
        this.appointmentRequests.add(request);
    }
}
