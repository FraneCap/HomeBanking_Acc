package com.example.demo.Models;

import Utils.CardUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    private String number;

    private LocalDateTime creationDate;

    private Double balance;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }



    public Account() {
    }

    public Account(Client client) {
        this.number = "VIN" + CardUtils.generateNumbers(3);
        this.creationDate = LocalDateTime.now();
        this.balance = 00.00;
        this.client = client;
    }

    public Account(String number, LocalDateTime creationDate, Double balance, Client client) {
        this.number ="VIN" + CardUtils.generateNumbers(3);
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
    }



    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}



