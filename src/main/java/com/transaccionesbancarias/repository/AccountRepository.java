package com.transaccionesbancarias.repository;

import com.transaccionesbancarias.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(Long accountNumber);

    Account findByOwnerName(String ownerName);

}
