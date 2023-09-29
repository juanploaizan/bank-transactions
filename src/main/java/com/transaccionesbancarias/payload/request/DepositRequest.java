package com.transaccionesbancarias.payload.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DepositRequest {
    @Positive(message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Initial balance must have a maximum of 10 digits and 2 decimals")
    private Double amount;
}
