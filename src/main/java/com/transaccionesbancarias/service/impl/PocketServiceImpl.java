package com.transaccionesbancarias.service.impl;

import com.transaccionesbancarias.exception.AccountNotFoundException;
import com.transaccionesbancarias.exception.InsufficientFundsException;
import com.transaccionesbancarias.exception.PocketExistsException;
import com.transaccionesbancarias.exception.PocketNotFoundException;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.repository.AccountRepository;
import com.transaccionesbancarias.repository.PocketRepository;
import com.transaccionesbancarias.service.PocketService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PocketServiceImpl implements PocketService {

    private final PocketRepository pocketRepository;

    private final AccountRepository accountRepository;

    public PocketServiceImpl(PocketRepository pocketRepository, AccountRepository accountRepository) {
        this.pocketRepository = pocketRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public Pocket createPocket(Long accountNumber, String name, Double initialValue) {

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account with number " + accountNumber + " not found");
        }

        if (initialValue > account.getBalance()) {
            throw new InsufficientFundsException("Insufficient funds in account " + accountNumber + " to create pocket");
        }

        Pocket pocket = pocketRepository.findByAccount_AccountNumberAndName(accountNumber, name);

        if (pocket != null) {
            throw new PocketExistsException("Pocket with name " + name + " already exists in account " + accountNumber);
        }

        account.setBalance(account.getBalance() - initialValue);

        pocket = Pocket.builder()
                .pocketNumber("P"+(pocketRepository.count() + 1))
                .name(name)
                .account(account)
                .balance(initialValue)
                .build();

        accountRepository.save(account);
        return pocketRepository.save(pocket);
    }

    @Override
    public Pocket transferFromAccount(Long accountNumber, String pocketNumber, Double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException("Account with number " + accountNumber + " not found");
        }

        if (amount > account.getBalance()) {
            throw new InsufficientFundsException("Insufficient funds in account " + accountNumber + " to create pocket");
        }

        Pocket pocket = pocketRepository.findByAccount_AccountNumber(accountNumber);

        if (pocket == null) {
            throw new PocketNotFoundException("Pocket with number " + pocketNumber + " with the account number " + accountNumber + " not found");
        }

        account.setBalance(account.getBalance() - amount);
        pocket.setBalance(pocket.getBalance() + amount);

        accountRepository.save(account);

        return pocketRepository.save(pocket);
    }

    @Override
    public Set<Pocket> getPocketsByAccountNumber(Long accountNumber) {
        return null;
    }
}
