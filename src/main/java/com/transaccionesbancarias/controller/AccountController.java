package com.transaccionesbancarias.controller;

import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.payload.request.AccountRequest;
import com.transaccionesbancarias.payload.request.DepositRequest;
import com.transaccionesbancarias.payload.request.TransferRequest;
import com.transaccionesbancarias.payload.response.SuccessfulResponse;
import com.transaccionesbancarias.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}/pockets")
    public ResponseEntity<Set<Pocket>> getAccountPockets(@PathVariable Long accountNumber) {
        Set<Pocket> pockets = accountService.getAccountPockets(accountNumber);
        return ResponseEntity.ok(pockets);
    }

    @PostMapping
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Account> deposit(@PathVariable Long accountNumber, @Valid @RequestBody DepositRequest request) {

        Account account = accountService.deposit(accountNumber, request.getAmount());

        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SuccessfulResponse> transfer(@Valid @RequestBody TransferRequest request) {

        accountService.transfer(request.getOriginAccountNumber(), request.getDestinationAccountNumber(), request.getAmount());

        return ResponseEntity.ok(new SuccessfulResponse("Transfer successful", "Amount: " + request.getAmount() + " from account: " + request.getOriginAccountNumber() + " to account: " + request.getDestinationAccountNumber()));
    }

}
