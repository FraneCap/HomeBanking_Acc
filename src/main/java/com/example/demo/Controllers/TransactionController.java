package com.example.demo.Controllers;

import com.example.demo.Dtos.TransactionDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Client;
import com.example.demo.Models.Transaction;
import com.example.demo.Models.TransactionType;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.ClientRepository;
import com.example.demo.Repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(toList());
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    @GetMapping("/transactions/byDateBetween/{date1}/{date2}")
    public List<TransactionDTO> getByDateBetween(@PathVariable String date1,@PathVariable String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return transactionRepository.findByCreationDateBetween((LocalDateTime.parse(date1, formatter)),(LocalDateTime.parse(date2, formatter))).stream().map(TransactionDTO::new).collect(toList());
    }


    @GetMapping("/transactions/byAmountBetween")
    public List<TransactionDTO> getByAmountBetween(@RequestParam double amount1, double amount2){
        return transactionRepository.findByAmountBetween(amount1, amount2).stream().map(TransactionDTO::new).collect(toList());
    }

    @GetMapping("/transactions/type/{type}")
    public List<TransactionDTO> getByType(@PathVariable TransactionType type){
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(toList());
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createdTransactionBetweenAccounts(Authentication authentication, @RequestParam Double amount, @RequestParam String description,
                                                                    @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber) {
        try {
            Client client = clientRepository.findByEmailIgnoreCase(authentication.getName()).get();
            if (!(amount <= 0.00) && !(description.isEmpty()) && !(fromAccountNumber.isEmpty()) && !(toAccountNumber.isEmpty())) {
                Optional<Account> optionalFromAccount = accountRepository.findByNumber(fromAccountNumber);
                Optional<Account> optionalToAccount = accountRepository.findByNumber(toAccountNumber);
                if (!(fromAccountNumber.equals(toAccountNumber)) && optionalFromAccount.isPresent()) {//valuamos q los numeros sean diferentes
                    Set<Account> accountsClientAuthentication = client.getAccounts();//toma todas las cuentas del cliente autenticado
                    if (accountsClientAuthentication.contains(optionalFromAccount.get()) &&
                            optionalToAccount.isPresent() &&
                            optionalFromAccount.get().getBalance() >= amount) {
                        Transaction transactionFrom = new Transaction(optionalFromAccount.get(), TransactionType.DEBIT, amount, description, LocalDateTime.now());
                        transactionRepository.save(transactionFrom);
                        Transaction transactionTo = new Transaction(optionalToAccount.get(), TransactionType.CREDIT, amount, description, LocalDateTime.now());
                        transactionRepository.save(transactionTo);
                        return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);  //account, transactiontype, double, string, localdatetime
                    } else {
                        return new ResponseEntity<>("Transaction error", HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Empty parameters", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<>("Unexpected", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}