package byurens.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import byurens.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Staffs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Staff {
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank
    @DecimalMin(value = "0.0", inclusive = false, message = "initial hourly pay can't be minus")
    @Column(nullable = false, name = "hourly_rate",scale = 2)
    private BigDecimal hourlyRate;

    @NotBlank
    @Column(nullable = false, name = "hire_date")
    private LocalDate hireDate;
}
