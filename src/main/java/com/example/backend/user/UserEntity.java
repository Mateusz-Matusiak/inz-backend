package com.example.backend.user;

import com.example.backend.animal.adoption.AdoptionSurveyEntity;
import com.example.backend.animal.adoption.WalkEntity;
import com.example.backend.user.address.AddressEntity;
import com.example.backend.user.role.RoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    @OneToOne
    private AddressEntity address;
    //todo add address entity
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleEntity role;
    @OneToMany(mappedBy = "id")
    private List<AdoptionSurveyEntity> adoptionSurveys;
    @OneToMany(mappedBy = "id")
    private List<WalkEntity> walks;


    public UserEntity(
            String firstName, String lastName, String email,
            String password, String phoneNumber, RoleEntity role
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
