package com.example.backend.user;

import com.example.backend.animal.adoption.AdoptionSurveyEntity;
import com.example.backend.animal.adoption.WalkEntity;
import com.example.backend.user.address.AddressEntity;
import com.example.backend.user.role.RoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {
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
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private CredentialsProvider provider;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleEntity role;
    @OneToMany(mappedBy = "id")
    private List<AdoptionSurveyEntity> adoptionSurveys;
    @OneToMany(mappedBy = "id")
    private List<WalkEntity> walks;

    @NotNull
    @Column(nullable = false)
    private boolean isActive;

    @NotNull
    @Column(nullable = false)
    private boolean isNotBlocked;


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
        this.provider = CredentialsProvider.INTERNAL;
        this.isActive = false;
        this.isNotBlocked = true;
    }

    public UserEntity(
            String firstName, String lastName, String email,
            String password, String phoneNumber, CredentialsProvider provider,
            RoleEntity role
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.provider = provider;
        this.role = role;
        this.isActive = true;
        this.isNotBlocked = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(getRole().getName());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNotBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
