package byurens.dto;

import java.util.UUID;

import byurens.enums.TaskTarget;

public record CategoryResponse(
    UUID id,
    String label,
    Boolean isActive,
    TaskTarget taskTarget
) {}
