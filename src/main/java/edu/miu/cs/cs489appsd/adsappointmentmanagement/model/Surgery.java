package edu.miu.cs.cs489appsd.adsappointmentmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "surgeries")
@NoArgsConstructor
@Data
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String surgeryNumber;

    @Column(name = "name", nullable = false, length = 32)
    private String surgeryName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address location;

    @Column(nullable = false, unique = true, length = 12)
    private String phone;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}