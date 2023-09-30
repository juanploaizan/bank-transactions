package com.transaccionesbancarias.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pockets")
public class Pocket {

    @Id
    private String pocketNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double balance;

    @ManyToOne
    @JsonBackReference
    private Account account;

}
