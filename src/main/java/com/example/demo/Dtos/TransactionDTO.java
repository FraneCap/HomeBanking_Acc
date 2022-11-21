package com.example.demo.Dtos;

import com.example.demo.Models.Account;
import com.example.demo.Models.Transaction;
import com.example.demo.Models.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionDTO {

    private Long id;

    private Account account;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;


    public TransactionDTO(Long id, Account account, TransactionType type, Double amount, String description, LocalDateTime creationDate) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = creationDate;
    }

    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getCreationDate();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;    }



    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        if (date.toLocalDate().equals(LocalDate.now()) ){

            return description + " - "+ type + " - " + "día actual";
        }
        else if(date.toLocalDate().isBefore(LocalDate.now())){

            return description+ " - " +type + " - dia anterior";
        }else
        return description + " - " +  type + " - dia posterior";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }





}
