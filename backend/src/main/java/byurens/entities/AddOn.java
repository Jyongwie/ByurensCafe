package byurens.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Add_Ons")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddOn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull
    @JsonBackReference
    @JoinColumn(name = "add_on_group_id", nullable = false)
    private AddOnGroup addOnGroup;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @DecimalMin(value = "0.0", message = "price can't be negative")
    @Column(nullable = false)
    private BigDecimal price;

    @NotBlank
    @DecimalMin(value = "0.0", message = "cost can't be negative")
    @Column(nullable = false)
    private BigDecimal capital;

    @Column(nullable = false, name = "is_available")
    private boolean isAvailable = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
