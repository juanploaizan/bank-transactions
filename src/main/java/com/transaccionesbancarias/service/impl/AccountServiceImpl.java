package com.transaccionesbancarias.service.impl;

import com.transaccionesbancarias.exception.AccountExistsException;
import com.transaccionesbancarias.exception.AccountNotFoundException;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.repository.AccountRepository;
import com.transaccionesbancarias.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {

        Account newAccount = accountRepository.findByOwnerName(account.getOwnerName());

        if (newAccount != null){
            throw new AccountExistsException("Account already exists with owner name: " + account.getOwnerName());
        }

        return accountRepository.save(account);
    }

    @Override
    public Account deposit(Long accountNumber, Double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null){
            throw new AccountNotFoundException("Account does not exists with account number: " + accountNumber);
        }

        account.setBalance(account.getBalance() + amount);

        return accountRepository.save(account);
    }

    @Override
    public Boolean transfer(Long originAccountNumber, Long targetAccountNumber, Double amount) {
        return null;
    }

    @Override
    public Account getAccount(Long accountNumber) {
        return null;
    }
}
