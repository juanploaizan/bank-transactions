package com.transaccionesbancarias.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaccionesbancarias.model.Account;
import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.payload.request.PocketRequest;
import com.transaccionesbancarias.payload.request.PocketTransferRequest;
import com.transaccionesbancarias.service.PocketService;

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

@ContextConfiguration(classes = {PocketController.class})
@ExtendWith(SpringExtension.class)
class PocketControllerTest {
    @Autowired
    private PocketController pocketController;

    @MockBean
    private PocketService pocketService;

    /**
     * Method under test: {@link PocketController#createPocket(PocketRequest)}
     */
    @Test
    void testCreatePocket() throws Exception {
        PocketRequest pocketRequest = new PocketRequest();
        pocketRequest.setAccountNumber(1234567890L);
        pocketRequest.setInitialValue(10.0d);
        pocketRequest.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(pocketRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/pockets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(pocketController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    /**
     * Method under test: {@link PocketController#transferToPocket(PocketTransferRequest)}
     */
    @Test
    void testTransferToPocket() throws Exception {
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
        when(pocketService.transferFromAccount(Mockito.<Long>any(), Mockito.<String>any(), Mockito.<Double>any()))
                .thenReturn(pocket);

        PocketTransferRequest pocketTransferRequest = new PocketTransferRequest();
        pocketTransferRequest.setAccountNumber(1234567890L);
        pocketTransferRequest.setAmount(10.0d);
        pocketTransferRequest.setPocketNumber("42");
        String content = (new ObjectMapper()).writeValueAsString(pocketTransferRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/pockets/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(pocketController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"pocketNumber\":\"42\",\"name\":\"Name\",\"balance\":10.0}"));
    }
}

