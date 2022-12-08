package com.example.backend.user.address;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    private Long id;
    private String city;
    private String street;
    private String postalCode;
    private String country;
}
