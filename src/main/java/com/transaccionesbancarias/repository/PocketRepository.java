package com.transaccionesbancarias.repository;

import com.transaccionesbancarias.model.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PocketRepository extends JpaRepository<Pocket, String> {
}
