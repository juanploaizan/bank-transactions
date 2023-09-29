package com.transaccionesbancarias.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    @Setter
    private Integer id;

    @Setter
    private String username;

    @Setter
    private String email;

    private List<String> roles;
}
