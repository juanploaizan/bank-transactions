package com.transaccionesbancarias.service;

import com.transaccionesbancarias.model.Account;

import java.util.Set;

public interface AccountService {

    Account createAccount(Account account);

    Account deposit(Long accountNumber, Double amount);

    Boolean transfer(Long originAccountNumber, Long targetAccountNumber, Double amount);

    Account getAccount(Long accountNumber);
}
