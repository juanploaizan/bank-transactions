package com.transaccionesbancarias.service;

import com.transaccionesbancarias.model.Pocket;

import java.util.Set;

public interface PocketService {

    Pocket createPocket(Long accountNumber, String name, Double initialValue);

    Pocket transferFromAccount(Long accountNumber, String pocketNumber, Double amount);

    Set<Pocket> getPocketsByAccountNumber(Long accountNumber);

}
