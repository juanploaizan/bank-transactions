package com.transaccionesbancarias.service.impl;

import com.transaccionesbancarias.exception.AccountExistsException;
import com.transaccionesbancarias.exception.AccountNotFoundException;
import com.transaccionesbancarias.exception.InsufficientFundsException;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.repository.AccountRepository;
import com.transaccionesbancarias.repository.PocketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Esta clase proporciona pruebas unitarias para la clase AccountServiceImpl.
 */
class AccountServiceImplTest {

    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PocketRepository pocketRepository;

    /**
     * Este método se ejecuta antes de cada prueba para configurar el entorno de prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountRepository, pocketRepository);
    }

    // Pruebas para el método createAccount

    @Test
    void testCreateAccount() {
        Account account = new Account();
        account.setOwnerName("John Doe");

        when(accountRepository.findByOwnerName("John Doe")).thenReturn(null);
        when(accountRepository.save(account)).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals("John Doe", createdAccount.getOwnerName());

        verify(accountRepository, times(1)).findByOwnerName("John Doe");
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateAccountWithExistingOwnerName() {
        Account account = new Account();
        account.setOwnerName("John Doe");

        when(accountRepository.findByOwnerName("John Doe")).thenReturn(account);

        assertThrows(AccountExistsException.class, () -> accountService.createAccount(account));

        verify(accountRepository, times(1)).findByOwnerName("John Doe");
        verify(accountRepository, never()).save(any());
    }

    // Pruebas para el método deposit

    @Test
    void testDeposit() {
        Account account = new Account();
        account.setAccountNumber(123L);
        account.setBalance(1000.0);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = accountService.deposit(123L, 500.0);

        assertNotNull(updatedAccount);
        assertEquals(1500.0, updatedAccount.getBalance());

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDepositWithNonExistingAccount() {
        when(accountRepository.findByAccountNumber(123L)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.deposit(123L, 500.0));

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        verify(accountRepository, never()).save(any());
    }

    // Pruebas para el método transfer

    @Test
    void testTransfer() {
        Account originAccount = new Account();
        originAccount.setAccountNumber(123L);
        originAccount.setBalance(1000.0);

        Account targetAccount = new Account();
        targetAccount.setAccountNumber(456L);
        targetAccount.setBalance(2000.0);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(originAccount);
        when(accountRepository.findByAccountNumber(456L)).thenReturn(targetAccount);
        when(accountRepository.save(originAccount)).thenReturn(originAccount);
        when(accountRepository.save(targetAccount)).thenReturn(targetAccount);

        accountService.transfer(123L, 456L, 500.0);

        assertEquals(500.0, originAccount.getBalance());
        assertEquals(2500.0, targetAccount.getBalance());

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        verify(accountRepository, times(1)).findByAccountNumber(456L);
        verify(accountRepository, times(1)).save(originAccount);
        verify(accountRepository, times(1)).save(targetAccount);
    }

    @Test
    void testTransferWithInsufficientFunds() {
        Account originAccount = new Account();
        originAccount.setAccountNumber(123L);
        originAccount.setBalance(500.0);

        Account targetAccount = new Account();
        targetAccount.setAccountNumber(456L);
        targetAccount.setBalance(2000.0);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(originAccount);
        when(accountRepository.findByAccountNumber(456L)).thenReturn(targetAccount);

        assertThrows(InsufficientFundsException.class, () -> accountService.transfer(123L, 456L, 1000.0));

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        verify(accountRepository, times(1)).findByAccountNumber(456L);
        verify(accountRepository, never()).save(any());
    }

    // Pruebas para el método getAccount

    @Test
    void testGetAccount() {
        Account account = new Account();
        account.setAccountNumber(123L);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(account);

        Account retrievedAccount = accountService.getAccount(123L);

        assertNotNull(retrievedAccount);
        assertEquals(123, retrievedAccount.getAccountNumber());

        verify(accountRepository, times(1)).findByAccountNumber(123L);
    }

    @Test
    void testGetAccountWithNonExistingAccount() {
        when(accountRepository.findByAccountNumber(123L)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(123L));

        verify(accountRepository, times(1)).findByAccountNumber(123L);
    }

    // Pruebas para el método getAccountPockets

    @Test
    void testGetAccountPockets() {

    }

    @Test
    void testGetAccountPocketsWithNonExistingAccount() {
        when(accountRepository.findByAccountNumber(123L)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountPockets(123L));

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        verify(pocketRepository, never()).findPocketsByAccount_AccountNumber(anyLong());
    }

    // Pruebas adicionales

    @Test
    void testDepositWithNegativeAmount() {
        Account account = new Account();
        account.setAccountNumber(123L);
        account.setBalance(1000.0);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(account);

        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(123L, -500.0));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferWithNonExistingOriginAccount() {
        when(accountRepository.findByAccountNumber(123L)).thenReturn(null);
        Account targetAccount = new Account();
        targetAccount.setAccountNumber(456L);

        when(accountRepository.findByAccountNumber(456L)).thenReturn(targetAccount);

        assertThrows(AccountNotFoundException.class, () -> accountService.transfer(123L, 456L, 500.0));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferWithNonExistingTargetAccount() {
        Account originAccount = new Account();
        originAccount.setAccountNumber(123L);
        originAccount.setBalance(1000.0);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(originAccount);
        when(accountRepository.findByAccountNumber(456L)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.transfer(123L, 456L, 500.0));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferWithNegativeAmount() {
        Account originAccount = new Account();
        originAccount.setAccountNumber(123L);
        originAccount.setBalance(1000.0);

        Account targetAccount = new Account();
        targetAccount.setAccountNumber(456L);
        targetAccount.setBalance(2000.0);

        when(accountRepository.findByAccountNumber(123L)).thenReturn(originAccount);
        when(accountRepository.findByAccountNumber(456L)).thenReturn(targetAccount);
        when(accountRepository.save(originAccount)).thenReturn(originAccount);
        when(accountRepository.save(targetAccount)).thenReturn(targetAccount);

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer(123L, 456L, -500.0));

        verify(accountRepository, never()).save(any());
    }

}
