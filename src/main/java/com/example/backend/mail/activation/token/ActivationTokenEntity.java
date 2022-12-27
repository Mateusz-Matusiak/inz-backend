package com.example.backend.mail.activation.token;

import com.example.backend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "activation_tokens")
public class ActivationTokenEntity {

    @Id
    @NotNull
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expireTime;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
