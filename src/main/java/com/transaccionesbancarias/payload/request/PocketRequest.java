package com.transaccionesbancarias.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PocketRequest {

    @Digits(integer = 16, fraction = 0, message = "Account number must be a number with max 16 digits")
    @Positive(message = "Account number must be a positive number")
    private Long accountNumber;

    @NotBlank(message = "Owner name must not be blank")
    @NotEmpty(message = "Owner name must not be empty")
    @Size(min = 3, max = 50, message = "Owner name must be between 3 and 50 characters")
    private String name;

    @Digits(integer = 10, fraction = 2, message = "Initial balance must have a maximum of 10 digits and 2 decimals")
    @Positive(message = "Initial balance must be greater than zero")
    private Double initialValue;

}
