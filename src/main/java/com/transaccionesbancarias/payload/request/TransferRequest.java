package com.transaccionesbancarias.payload.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferRequest {

    @Digits(integer = 16, fraction = 0, message = "Account number must be a number with max 16 digits")
    @Positive(message = "Destination account number must be a positive number")
    private Long originAccountNumber;

    @Digits(integer = 16, fraction = 0, message = "Account number must be a number with max 16 digits")
    @Positive(message = "Destination account number must be a positive number")
    private Long destinationAccountNumber;

    @Positive(message = "Amount must be a positive number")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a number with 10 digits and 2 decimals")
    private Double amount;

}
