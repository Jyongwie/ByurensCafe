package byurens.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import byurens.enums.UnitMeasurement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Inventory_Items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "unit_measurement")
    private UnitMeasurement unitMeasurement;

    @NotNull
    @DecimalMin(value = "0.0", message = "Quantity can't be negative")
    @Column(nullable = false, name = "current_stock")
    private BigDecimal currentStock = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.0", message = "Quantity can't be negative")
    @Column(nullable = false, name = "low_stock_threshold")
    private BigDecimal lowStockThreshold = BigDecimal.ZERO;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    @JsonIgnore
    public boolean isLowStock() {
        return this.currentStock.compareTo(this.lowStockThreshold) <= 0;
    }
}
