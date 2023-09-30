package com.transaccionesbancarias.repository;

import com.transaccionesbancarias.model.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PocketRepository extends JpaRepository<Pocket, String> {

    Pocket findByAccount_AccountNumber(Long accountNumber);
    Pocket findByAccount_AccountNumberAndName(Long accountNumber, String name);

    Set<Pocket> findPocketsByAccount_AccountNumber(Long accountNumber);

}
