package byurens.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import byurens.enums.Role;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HireStaffRequest(
    @NotBlank
    @Email(message = "must be a valid email format")
    String email,

    @NotBlank(message = "password is required")
    String rawPassword,

    @NotBlank(message = "phone number is required")
    String phoneNumber,

    @NotBlank(message = "name is required")
    String name,

    @NotNull(message = "role is required")
    Role role,

    @NotNull(message = "hourly rate is required")
    @DecimalMin(value = "0.0")
    BigDecimal hourlyRate,

    @NotNull(message = "hire date is required")
    LocalDate hireDate
) {}
