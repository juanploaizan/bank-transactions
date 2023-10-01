package com.transaccionesbancarias.service.impl;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaccionesbancarias.exception.InsufficientFundsException;
import com.transaccionesbancarias.exception.PocketExistsException;
import com.transaccionesbancarias.exception.PocketNotFoundException;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.repository.AccountRepository;
import com.transaccionesbancarias.repository.PocketRepository;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PocketServiceImpl.class})
@ExtendWith(SpringExtension.class)
class PocketServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PocketRepository pocketRepository;

    @Autowired
    private PocketServiceImpl pocketServiceImpl;

    /**
     * Method under test: {@link PocketServiceImpl#createPocket(Long, String, Double)}
     */
    @Test
    void testCreatePocket() {
        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());

        Pocket pocket = new Pocket();
        pocket.setAccount(account);
        pocket.setBalance(10.0d);
        pocket.setName("Name");
        pocket.setPocketNumber("42");
        when(pocketRepository.findByAccount_AccountNumberAndName(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(pocket);

        Account account2 = new Account();
        account2.setAccountNumber(1234567890L);
        account2.setBalance(10.0d);
        account2.setOwnerName("Owner Name");
        account2.setPockets(new HashSet<>());
        when(accountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(account2);
        assertThrows(PocketExistsException.class, () -> pocketServiceImpl.createPocket(1234567890L, "Name", 10.0d));
        verify(pocketRepository).findByAccount_AccountNumberAndName(Mockito.<Long>any(), Mockito.any());
        verify(accountRepository).findByAccountNumber(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link PocketServiceImpl#createPocket(Long, String, Double)}
     */
    @Test
    void testCreatePocket2() {
        when(pocketRepository.findByAccount_AccountNumberAndName(Mockito.<Long>any(), Mockito.any()))
                .thenThrow(new PocketNotFoundException("An error occurred"));

        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());
        when(accountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(account);
        assertThrows(PocketNotFoundException.class, () -> pocketServiceImpl.createPocket(1234567890L, "Name", 10.0d));
        verify(pocketRepository).findByAccount_AccountNumberAndName(Mockito.<Long>any(), Mockito.any());
        verify(accountRepository).findByAccountNumber(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link PocketServiceImpl#createPocket(Long, String, Double)}
     */
    @Test
    void testCreatePocket3() {
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(0.5d);
        doNothing().when(account).setAccountNumber(Mockito.<Long>any());
        doNothing().when(account).setBalance(Mockito.<Double>any());
        doNothing().when(account).setOwnerName(Mockito.any());
        doNothing().when(account).setPockets(Mockito.any());
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());
        when(accountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(account);
        assertThrows(InsufficientFundsException.class, () -> pocketServiceImpl.createPocket(1234567890L, "Name", 10.0d));
        verify(accountRepository).findByAccountNumber(Mockito.<Long>any());
        verify(account).getBalance();
        verify(account).setAccountNumber(Mockito.<Long>any());
        verify(account).setBalance(Mockito.<Double>any());
        verify(account).setOwnerName(Mockito.any());
        verify(account).setPockets(Mockito.any());
    }

    /**
     * Method under test: {@link PocketServiceImpl#transferFromAccount(Long, String, Double)}
     */
    @Test
    void testTransferFromAccount() {
        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());

        Pocket pocket = new Pocket();
        pocket.setAccount(account);
        pocket.setBalance(10.0d);
        pocket.setName("Name");
        pocket.setPocketNumber("42");

        Account account2 = new Account();
        account2.setAccountNumber(1234567890L);
        account2.setBalance(10.0d);
        account2.setOwnerName("Owner Name");
        account2.setPockets(new HashSet<>());

        Pocket pocket2 = new Pocket();
        pocket2.setAccount(account2);
        pocket2.setBalance(10.0d);
        pocket2.setName("Name");
        pocket2.setPocketNumber("42");
        when(pocketRepository.save(Mockito.any())).thenReturn(pocket2);
        when(pocketRepository.findByAccount_AccountNumber(Mockito.<Long>any())).thenReturn(pocket);

        Account account3 = new Account();
        account3.setAccountNumber(1234567890L);
        account3.setBalance(10.0d);
        account3.setOwnerName("Owner Name");
        account3.setPockets(new HashSet<>());

        Account account4 = new Account();
        account4.setAccountNumber(1234567890L);
        account4.setBalance(10.0d);
        account4.setOwnerName("Owner Name");
        account4.setPockets(new HashSet<>());
        when(accountRepository.save(Mockito.any())).thenReturn(account4);
        when(accountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(account3);
        assertSame(pocket2, pocketServiceImpl.transferFromAccount(1234567890L, "42", 10.0d));
        verify(pocketRepository).findByAccount_AccountNumber(Mockito.<Long>any());
        verify(pocketRepository).save(Mockito.any());
        verify(accountRepository).findByAccountNumber(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.any());
    }

    /**
     * Method under test: {@link PocketServiceImpl#transferFromAccount(Long, String, Double)}
     */
    @Test
    void testTransferFromAccount2() {
        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());

        Pocket pocket = new Pocket();
        pocket.setAccount(account);
        pocket.setBalance(10.0d);
        pocket.setName("Name");
        pocket.setPocketNumber("42");

        Account account2 = new Account();
        account2.setAccountNumber(1234567890L);
        account2.setBalance(10.0d);
        account2.setOwnerName("Owner Name");
        account2.setPockets(new HashSet<>());

        Pocket pocket2 = new Pocket();
        pocket2.setAccount(account2);
        pocket2.setBalance(10.0d);
        pocket2.setName("Name");
        pocket2.setPocketNumber("42");
        when(pocketRepository.save(Mockito.any())).thenReturn(pocket2);
        when(pocketRepository.findByAccount_AccountNumber(Mockito.<Long>any())).thenReturn(pocket);

        Account account3 = new Account();
        account3.setAccountNumber(1234567890L);
        account3.setBalance(10.0d);
        account3.setOwnerName("Owner Name");
        account3.setPockets(new HashSet<>());
        when(accountRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException("foo"));
        when(accountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(account3);
        assertThrows(IllegalArgumentException.class,
                () -> pocketServiceImpl.transferFromAccount(1234567890L, "42", 10.0d));
        verify(pocketRepository).findByAccount_AccountNumber(Mockito.<Long>any());
        verify(accountRepository).findByAccountNumber(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.any());
    }

    /**
     * Method under test: {@link PocketServiceImpl#transferFromAccount(Long, String, Double)}
     */
    @Test
    void testTransferFromAccount3() {
        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());
        Pocket pocket = mock(Pocket.class);
        when(pocket.getBalance()).thenReturn(10.0d);
        doNothing().when(pocket).setAccount(Mockito.any());
        doNothing().when(pocket).setBalance(Mockito.<Double>any());
        doNothing().when(pocket).setName(Mockito.any());
        doNothing().when(pocket).setPocketNumber(Mockito.any());
        pocket.setAccount(account);
        pocket.setBalance(10.0d);
        pocket.setName("Name");
        pocket.setPocketNumber("42");

        Account account2 = new Account();
        account2.setAccountNumber(1234567890L);
        account2.setBalance(10.0d);
        account2.setOwnerName("Owner Name");
        account2.setPockets(new HashSet<>());

        Pocket pocket2 = new Pocket();
        pocket2.setAccount(account2);
        pocket2.setBalance(10.0d);
        pocket2.setName("Name");
        pocket2.setPocketNumber("42");
        when(pocketRepository.save(Mockito.any())).thenReturn(pocket2);
        when(pocketRepository.findByAccount_AccountNumber(Mockito.<Long>any())).thenReturn(pocket);

        Account account3 = new Account();
        account3.setAccountNumber(1234567890L);
        account3.setBalance(10.0d);
        account3.setOwnerName("Owner Name");
        account3.setPockets(new HashSet<>());

        Account account4 = new Account();
        account4.setAccountNumber(1234567890L);
        account4.setBalance(10.0d);
        account4.setOwnerName("Owner Name");
        account4.setPockets(new HashSet<>());
        when(accountRepository.save(Mockito.any())).thenReturn(account4);
        when(accountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(account3);
        assertSame(pocket2, pocketServiceImpl.transferFromAccount(1234567890L, "42", 10.0d));
        verify(pocketRepository).findByAccount_AccountNumber(Mockito.<Long>any());
        verify(pocketRepository).save(Mockito.any());
        verify(pocket).getBalance();
        verify(pocket).setAccount(Mockito.any());
        verify(pocket, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(pocket).setName(Mockito.any());
        verify(pocket).setPocketNumber(Mockito.any());
        verify(accountRepository).findByAccountNumber(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.any());
    }

    /**
     * Method under test: {@link PocketServiceImpl#getPocketsByAccountNumber(Long)}
     */
    @Test
    void testGetPocketsByAccountNumber() {
        assertNull(pocketServiceImpl.getPocketsByAccountNumber(1234567890L));
    }
}

