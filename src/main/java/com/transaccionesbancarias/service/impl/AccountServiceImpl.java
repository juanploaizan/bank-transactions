package com.transaccionesbancarias.service.impl;

import com.transaccionesbancarias.exception.AccountExistsException;
import com.transaccionesbancarias.exception.AccountNotFoundException;
import com.transaccionesbancarias.exception.InsufficientFundsException;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.repository.AccountRepository;
import com.transaccionesbancarias.repository.PocketRepository;
import com.transaccionesbancarias.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PocketRepository pocketRepository;

    public AccountServiceImpl(AccountRepository accountRepository, PocketRepository pocketRepository) {
        this.accountRepository = accountRepository;
        this.pocketRepository = pocketRepository;
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

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null){
            throw new AccountNotFoundException("Account does not exists with account number: " + accountNumber);
        }

        account.setBalance(account.getBalance() + amount);

        return accountRepository.save(account);
    }

    @Override
    public void transfer(Long originAccountNumber, Long destinationAccountNumber, Double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account originAccount = accountRepository.findByAccountNumber(originAccountNumber);
        Account targetAccount = accountRepository.findByAccountNumber(destinationAccountNumber);

        if (originAccount == null) {
            throw new AccountNotFoundException("Account does not exists with account number: " + originAccountNumber);
        }

        if (targetAccount == null) {
            throw new AccountNotFoundException("Account does not exists with account number: " + destinationAccountNumber);
        }

        if (originAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        originAccount.setBalance(originAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        accountRepository.save(originAccount);
        accountRepository.save(targetAccount);
    }

    @Override
    public Account getAccount(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account does not exists with account number: " + accountNumber);
        }
        return account;
    }

    @Override
    public Set<Pocket> getAccountPockets(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account does not exists with account number: " + accountNumber);
        }
        return pocketRepository.findPocketsByAccount_AccountNumber(accountNumber);
    }
}
