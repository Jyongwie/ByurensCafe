package byurens.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @NotNull
    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(nullable = false, name = "is_active")
    private boolean isActive;

    @Column(name = "is_email_verified")
    private boolean isEmailVerified = false;

    @Column(name = "is_phone_number_verified")
    private boolean isPhoneNumberVerified = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
