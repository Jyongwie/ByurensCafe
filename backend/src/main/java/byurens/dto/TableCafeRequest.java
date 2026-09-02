package byurens.dto;

import byurens.enums.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TableCafeRequest(
    @NotBlank
    String tableIdentifier,

    @Min(1)
    @NotNull
    Integer capacity,
    
    TableStatus status
) {}
