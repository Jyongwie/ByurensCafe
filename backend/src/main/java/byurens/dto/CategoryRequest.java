package byurens.dto;

import java.util.List;

import byurens.enums.TaskTarget;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
    @NotBlank 
    String label,

    @NotNull 
    Boolean isActive,
    
    TaskTarget taskTarget,
    List<ProductRequest> products
) {}
