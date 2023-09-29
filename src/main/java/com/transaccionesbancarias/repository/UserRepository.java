package com.transaccionesbancarias.repository;

import com.transaccionesbancarias.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String userName);
    Boolean existsByUsername(String userName);
    Boolean existsByEmail(String email);

}
