package edu.miu.cs.cs489appsd.adsappointmentmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Long id;
    @Column(unique = true)
    private String unitNo;
    private String street;
    private String city;
    private String state;
    private Integer zip;

    public Address(String unitNo, String street, String city, String state, int zip) {
    }
}
