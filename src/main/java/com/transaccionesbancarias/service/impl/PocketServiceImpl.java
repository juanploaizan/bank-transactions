package com.transaccionesbancarias.service.impl;

import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.repository.PocketRepository;
import com.transaccionesbancarias.service.PocketService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PocketServiceImpl implements PocketService {

    private final PocketRepository pocketRepository;

    public PocketServiceImpl(PocketRepository pocketRepository) {
        this.pocketRepository = pocketRepository;
    }


    @Override
    public Pocket createPocket(Pocket pocket) {
        return null;
    }

    @Override
    public Pocket transferFromAccount(Long accountNumber, String pocketNumber, Double amount) {
        return null;
    }

    @Override
    public Set<Pocket> getPocketsByAccountNumber(Long accountNumber) {
        return null;
    }
}
