package byurens.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddOnGroupRequest(
    @NotBlank
    String name,

    @Min(0)
    int minSelection,

    @Min(1)
    Integer maxSelection,

    List<AddOnRequest> addOns
) {}
