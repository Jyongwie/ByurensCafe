package byurens.dto;

import java.util.UUID;

import byurens.enums.TableStatus;

public record TableCafeResponse(
    UUID id,
    String tableIdentifier,
    Integer capacity,
    TableStatus status
) {}
