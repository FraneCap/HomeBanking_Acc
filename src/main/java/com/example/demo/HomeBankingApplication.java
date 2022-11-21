package com.example.demo;

import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class HomeBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBankingApplication.class, args);
    }

   /* @Autowired
    PasswordEncoder passwordEncoder;


    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
        return (args) -> {


            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1234"));
            clientRepository.save(client1);
            Client client2 = new Client("Pablo", "Martinez", "pablo.mart@mindhub.com", passwordEncoder.encode("4567"));
            clientRepository.save(client2);
            Client client3 = new Client("Emilia", "Guirao", "emi-gui94@mindhub.com", passwordEncoder.encode("6789"));
            clientRepository.save(client3);

            Account account1 = new Account("", LocalDateTime.now(), 5000.0, client1);
            accountRepository.save(account1);
            Account account2 = new Account("", LocalDateTime.now().plusDays(1), 7500.0, client1);
            accountRepository.save(account2);
            Account account3 = new Account("", LocalDateTime.now().minusDays(1), 8000.50, client2);
            accountRepository.save(account3);

            Transaction transaction1 = new Transaction(account1, TransactionType.CREDIT, 5000.00, "Recibo de sueldo", LocalDateTime.now());
            transactionRepository.save(transaction1);
            Transaction transaction2 = new Transaction(account1, TransactionType.DEBIT, 3500.00, "VEP monotributo", LocalDateTime.now());
            transactionRepository.save(transaction2);
            Transaction transaction3 = new Transaction(account1, TransactionType.DEBIT, 4000.00, "Expensas", LocalDateTime.now());
            transactionRepository.save(transaction3);
            Transaction transaction4 = new Transaction(account1, TransactionType.DEBIT, 450.10, "Impuesto de sellos", LocalDateTime.now());
            transactionRepository.save(transaction4);
            Transaction transaction5 = new Transaction(account2, TransactionType.DEBIT, 2950.10, "Transferencia entre cuentas", LocalDateTime.now());
            transactionRepository.save(transaction5);
            Transaction transaction6 = new Transaction(account1, TransactionType.CREDIT, 2950.10, "Transferencia entre cuentas", LocalDateTime.now());
            transactionRepository.save(transaction6);
            Transaction transaction7 = new Transaction(account3, TransactionType.CREDIT, 2050.00, "Clases particulares", LocalDateTime.now());
            transactionRepository.save(transaction7);
            Transaction transaction8 = new Transaction(account3, TransactionType.CREDIT, 2050.00, "Clases particulares", LocalDateTime.now());
            transactionRepository.save(transaction8);

            Loan loan1 = new Loan("Hipotecario", 500000.0, Arrays.asList(12, 24, 36, 48, 60));
            loanRepository.save(loan1);
            Loan loan2 = new Loan("Personal", 100000.0, Arrays.asList(6, 12, 24));
            loanRepository.save(loan2);
            Loan loan3 = new Loan("Automotriz", 300000.0, Arrays.asList(12, 24, 36));
            loanRepository.save(loan3);

            ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client1, loan1);
            clientLoanRepository.save(clientLoan1);
            ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client1, loan2);
            clientLoanRepository.save(clientLoan2);
            ClientLoan clientLoan3 = new ClientLoan(100000.00, 24, client2, loan2);
            clientLoanRepository.save(clientLoan3);
            ClientLoan clientLoan4 = new ClientLoan(200000.00, 36, client2, loan3);
            clientLoanRepository.save(clientLoan4);


            Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, "4434-8765-9123-4577", "774", client1);
            cardRepository.save(card1);
            Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, "4434-5678-7801-5500", "853", client1);
            cardRepository.save(card2);
            Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, "2234-5678-2124-4216", "621", client2);
            cardRepository.save(card3);


           /* Bank bank1 = new Bank("Nacion", "5524", 4);
            bankRepository.save(bank1);

            Card extraCard = new Card(CardType.DEBIT, CardColor.GOLD, "522" , client1, bank1);
            cardRepository.save(extraCard);
            CardType type, CardColor color, String cvv, Client client, Bank bank*/

        };
    //}


