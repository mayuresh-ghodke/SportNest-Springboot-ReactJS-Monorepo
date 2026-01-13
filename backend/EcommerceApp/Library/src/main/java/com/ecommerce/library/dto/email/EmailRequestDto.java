package com.ecommerce.library.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailRequestDto {
    @Email
    @NotEmpty
    private String newEmail;
}
