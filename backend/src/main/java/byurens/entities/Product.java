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
@Table(name = "Products")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @NotBlank
    @JsonBackReference
    @JoinColumn(nullable = false)
    private String category;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price can't be minus")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price can't be minus")
    @Column(nullable = false, name = "on_sale")
    private BigDecimal onSale;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price can't be minus")
    @Column(nullable = false)
    private BigDecimal capital;

    @Column(nullable = false, name = "is_available")
    private boolean isAvailable;

    @Column(nullable = false, name = "is_promo")
    private boolean isPromo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
