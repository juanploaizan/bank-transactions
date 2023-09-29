package com.transaccionesbancarias.service;

import com.transaccionesbancarias.model.Pocket;

import java.util.Set;

public interface PocketService {

    Pocket createPocket(Pocket pocket);

    Pocket transferFromAccount(Long accountNumber, String pocketNumber, Double amount);

    Set<Pocket> getPocketsByAccountNumber(Long accountNumber);

}
