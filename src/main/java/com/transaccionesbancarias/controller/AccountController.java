package com.transaccionesbancarias.controller;

import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.payload.request.AccountRequest;
import com.transaccionesbancarias.payload.request.DepositRequest;
import com.transaccionesbancarias.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequest account) {

        Account createdAccount = accountService.createAccount(
                Account.builder()
                        .ownerName(account.getOwnerName())
                        .balance(account.getInitialBalance())
                        .build()
        );

        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long accountNumber, @Valid @RequestBody DepositRequest request) {

        Account account = accountService.deposit(accountNumber, request.getAmount());

        return ResponseEntity.ok(account);
    }

}
