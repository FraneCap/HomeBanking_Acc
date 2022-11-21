package com.example.demo.Controllers;

import com.example.demo.Dtos.AccountDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Client;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Optional<Client> optionalClient = clientRepository.findByEmailIgnoreCase(authentication.getName());
        return optionalClient.map(client -> client.getAccounts().stream().map(AccountDTO :: new).collect(Collectors.toList())).orElse(null);
    }

    @GetMapping("/accounts/byNumber")
    public AccountDTO getByNumber(@RequestParam String number){
        return accountRepository.findByNumber(number).map(AccountDTO::new).orElse(null);
    }

    @GetMapping("/accounts/balance")
    public List<AccountDTO> getByBalanceGreater(@RequestParam double balance){
        return accountRepository.findByBalanceGreaterThan(balance).stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/creationDateBefore/{creationDate}")
    public List<AccountDTO> getByCreationDateBefore (@PathVariable String creationDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return accountRepository.findByCreationDateBefore(LocalDateTime.parse(creationDate, formatter)).stream().map(AccountDTO::new).collect(toList());
    }


    @PostMapping("/clients/{clientId}/accounts")
    public ResponseEntity<Object> createAccount(@PathVariable Long clientId) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(clientId);
            if (clientOptional.isPresent()) { //si existe el client:
                Account account = new Account(clientOptional.get()); //acá lo crea
                accountRepository.save(account);  // lo guarda

                return new ResponseEntity<>(HttpStatus.CREATED); //devuelve un created

            } else { //sino existe:
                return new ResponseEntity<>("Cliente inexistente. ", HttpStatus.INTERNAL_SERVER_ERROR); //avisa que ya no hay cuentas disponbles
            }
        } catch (Exception ex) { //si falla algo de arriba, devuelve este error
            ex.printStackTrace();
            return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR); //error inesperado
        }
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Optional<Client> clientOptional = clientRepository.findByEmailIgnoreCase(authentication.getName());

        if (clientOptional.isPresent()) {


            if (clientOptional.get().getAccounts().size() < 3) {
                Account account = new Account(clientOptional.get());
                accountRepository.save(account);

                return new ResponseEntity<>("201 created",HttpStatus.CREATED);


            } else {
                return new ResponseEntity<>("403 forbidden", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Cliente inexistente! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}