package com.transaccionesbancarias.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.payload.request.AccountRequest;
import com.transaccionesbancarias.payload.request.DepositRequest;
import com.transaccionesbancarias.payload.request.TransferRequest;
import com.transaccionesbancarias.service.AccountService;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AccountController.class})
@ExtendWith(SpringExtension.class)
class AccountControllerTest {
    @Autowired
    private AccountController accountController;

    @MockBean
    private AccountService accountService;

    /**
     * Method under test: {@link AccountController#getAccount(Long)}
     */
    @Test
    void testGetAccount() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());
        when(accountService.getAccount(Mockito.<Long>any())).thenReturn(account);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{accountNumber}",
                1234567890L);
        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"accountNumber\":1234567890,\"balance\":10.0,\"ownerName\":\"Owner Name\"}"));
    }

    /**
     * Method under test: {@link AccountController#getAccountPockets(Long)}
     */
    @Test
    void testGetAccountPockets() throws Exception {
        when(accountService.getAccountPockets(Mockito.<Long>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{accountNumber}/pockets",
                1234567890L);
        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link AccountController#createAccount(AccountRequest)}
     */
    @Test
    void testCreateAccount() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setInitialBalance(10.0d);
        accountRequest.setOwnerName("Owner Name");
        String content = (new ObjectMapper()).writeValueAsString(accountRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    /**
     * Method under test: {@link AccountController#deposit(Long, DepositRequest)}
     */
    @Test
    void testDeposit() throws Exception {
        Account account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBalance(10.0d);
        account.setOwnerName("Owner Name");
        account.setPockets(new HashSet<>());
        when(accountService.deposit(Mockito.<Long>any(), Mockito.<Double>any())).thenReturn(account);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAmount(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(depositRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/accounts/{accountNumber}/deposit", 1234567890L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"accountNumber\":1234567890,\"balance\":10.0,\"ownerName\":\"Owner Name\"}"));
    }

    /**
     * Method under test: {@link AccountController#transfer(TransferRequest)}
     */
    @Test
    void testTransfer() throws Exception {
        doNothing().when(accountService).transfer(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Double>any());

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(10.0d);
        transferRequest.setDestinationAccountNumber(3L);
        transferRequest.setOriginAccountNumber(3L);
        String content = (new ObjectMapper()).writeValueAsString(transferRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"message\":\"Transfer successful\",\"details\":\"Amount: 10.0 from account: 3 to account: 3\"}"));
    }
}

