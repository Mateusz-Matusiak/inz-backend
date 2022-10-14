package com.example.backend.user;

import com.example.backend.user.role.RoleEntity;
import lombok.Data;

import javax.persistence.*;

@Data
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
    //    private String address;
    //todo add address entity
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleEntity role;
//    @OneToMany(mappedBy = "adoption_surveys")
//    private List<AdoptionSurveyEntity> adoptionSurveys;
//    @OneToMany(mappedBy = "walks")
//    private List<WalkEntity> walks;
}
