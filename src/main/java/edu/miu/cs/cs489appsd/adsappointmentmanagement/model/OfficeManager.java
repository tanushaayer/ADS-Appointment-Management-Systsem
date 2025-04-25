package edu.miu.cs.cs489appsd.adsappointmentmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "officeManager")
@Data
@EqualsAndHashCode(callSuper = false)
public class OfficeManager extends User{

}
