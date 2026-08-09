package byurens.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Add_On_Groups")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddOnGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "addOnGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AddOn> addOns = new ArrayList<>();

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Min(0)
    @Column(nullable = false, name = "min_selection")
    private int minSelection;

    @Min(1)
    @Column(name = "max_selection")
    private Integer maxSelection;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
