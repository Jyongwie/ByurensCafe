package byurens.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import byurens.enums.Role;

public record StaffResponse(
    UUID id,
    String name,
    Role role,
    BigDecimal hourlyRate,
    LocalDate hireDate
) {}
