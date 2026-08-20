package byurens.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewCustomerRequest(
    @NotBlank
    @Email(message = "must be a valid email format")
    String email,

    @NotBlank(message = "password is required")
    String rawPassword,

    @NotBlank(message = "name is required")
    String name,

    @NotBlank(message = "phone number is required")
    String phoneNumber
) {}
