package com.transaccionesbancarias.controller;

import com.transaccionesbancarias.model.Pocket;
import com.transaccionesbancarias.payload.request.PocketRequest;
import com.transaccionesbancarias.payload.request.PocketTransferRequest;
import com.transaccionesbancarias.service.PocketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pockets")
public class PocketController {

    private final PocketService pocketService;

    public PocketController(PocketService pocketService) {
        this.pocketService = pocketService;
    }

    @PostMapping
    public ResponseEntity<Pocket> createPocket(@Valid @RequestBody PocketRequest pocketRequest) {

        Pocket createdPocket = pocketService.createPocket(
                pocketRequest.getAccountNumber(),
                pocketRequest.getName(),
                pocketRequest.getInitialValue()
        );

        return ResponseEntity.ok(createdPocket);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Pocket> transferToPocket(@Valid @RequestBody PocketTransferRequest request) {

        Pocket resultPocket = pocketService.transferFromAccount(
                request.getAccountNumber(),
                request.getPocketNumber(),
                request.getAmount()
        );

        return ResponseEntity.ok(resultPocket);
    }
}
