package com.transaccionesbancarias.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SuccessfulResponse {
    private String message;
    private String details;
}
