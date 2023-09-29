package com.transaccionesbancarias.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountRequest {

    @NotBlank(message = "Owner name must not be blank")
    @NotEmpty(message = "Owner name must not be empty")
    @Size(min = 3, max = 50, message = "Owner name must be between 3 and 50 characters")
    private String ownerName;

    @Digits(integer = 10, fraction = 2, message = "Initial balance must have a maximum of 10 digits and 2 decimals")
    @Positive(message = "Initial balance must be greater than zero")
    private Double initialBalance;
}
