package com.transaccionesbancarias.service;

import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.model.Pocket;

import java.util.Set;

public interface AccountService {

    Account createAccount(Account account);

    Account deposit(Long accountNumber, Double amount);

    void transfer(Long originAccountNumber, Long destinationAccountNumber, Double amount);

    Account getAccount(Long accountNumber);

    Set<Pocket> getAccountPockets(Long accountNumber);
}
