package byurens.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import byurens.enums.Size;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Product_Variants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull
    @JsonBackReference
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price can't be minus")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price can't be minus")
    @Column(nullable = false, name = "on_sale")
    private BigDecimal onSale;

    @NotNull
    @Min(0)
    @Column(nullable = false, name = "on_sale_percentage")
    private int onSalePercent;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price can't be minus")
    @Column(nullable = false)
    private BigDecimal capital;

    @Column(nullable = false, name = "is_promo")
    private boolean isPromo = false;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}
