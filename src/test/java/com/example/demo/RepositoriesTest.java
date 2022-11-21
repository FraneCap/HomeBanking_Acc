package com.example.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import Utils.CardUtils;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import org.hamcrest.text.CharSequenceLength;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest

@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {



    @Autowired

    LoanRepository loanRepository;



    @Test

    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }



    @Test

    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    // Test ClientController
  @Autowired
  ClientRepository clientRepository;

    @Test
    public void containsChart(){
        String email = clientRepository.findById(1L).get().getEmail();
        assertThat(email, containsString("@"));
    }

    @Test
    public void hasEmail(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasProperty("email", not(emptyString()))));
    }

    // Test de CardController
    @Autowired
    CardRepository cardRepository;
    @Test
    public void cardCvvIsCreated(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,everyItem(hasProperty("cvv",is(not(nullValue())))));
    }

    @Test
    public void cardNumberIsDifferent(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,everyItem(hasProperty("number",notNullValue(Integer.class))));
    }

    //TransactionController
    @Autowired
    TransactionRepository transactionRepository;
    public void accountNotEmpty() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, everyItem(hasProperty("account", not(emptyOrNullString()))));
    }

    // Test Account
    @Autowired
    AccountRepository accountRepository;
    @Test
    public void balanceIsGreatThanZero(){
        List<Account> accounts = accountRepository.findByBalanceGreaterThan(0);
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void createdDateLessThanNow(){
        String date = "2022-11-16 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Account> accounts = accountRepository.findByCreationDateBefore(LocalDateTime.parse(date, formatter));
        assertThat(accounts,is(not(empty())));
    }



    @Test
    public void generateNumbersWorks(){
        String cardNumber = CardUtils.generateNumbers(6);
        assertThat(cardNumber, CharSequenceLength.hasLength(6));
    }
}