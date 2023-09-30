package com.transaccionesbancarias.payload.request;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PocketTransferRequest {

    @Digits(integer = 16, fraction = 0, message = "Account number must be a number with max 16 digits")
    @Positive(message = "Destination account number must be a positive number")
    private Long accountNumber;

    @NotEmpty(message = "Pocket number must not be empty")
    @NotBlank(message = "Pocket number must not be blank")
    @Size(min = 1, max = 50, message = "Pocket number must be between 1 and 50 characters")
    private String pocketNumber;

    @Positive(message = "Amount must be a positive number")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a number with 10 digits and 2 decimals")
    private Double amount;

}
