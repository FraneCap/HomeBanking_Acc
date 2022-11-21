package com.example.demo.Controllers;

import com.example.demo.Dtos.ClientDTO;

import com.example.demo.Models.Account;
import com.example.demo.Models.Client;
import com.example.demo.Repositories.AccountRepository;
import com.example.demo.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @PostMapping("/clients/new")
    public ClientDTO newClient(@RequestBody Client newClient){
        Client createdClient = this.clientRepository.save(newClient);
        return new ClientDTO(createdClient);
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/lastName")
    public List<ClientDTO> getByLastName(@RequestParam String lastName) {
        return clientRepository.findByLastNameIgnoreCase(lastName).stream().map(ClientDTO::new).collect(toList());
    }


    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        Client client = this.clientRepository.findByEmailIgnoreCase(authentication.getName()).get();
        return new ClientDTO(client);
    }

    @GetMapping("/clients/firstName")
    public List<ClientDTO> getByFirstName(@RequestParam String firstName) {
        return clientRepository.findByFirstNameIgnoreCase(firstName).stream().map(ClientDTO::new).collect(toList());
    }


    @GetMapping("/clients/email/{email}")
    public ClientDTO findByEmail(@PathVariable String email) {
        return clientRepository.findByEmailIgnoreCase(email).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("clients/firstNameAndEmail")
        public List<ClientDTO> getClientsByFirstnameAndEmailIgnoreCase(@RequestParam String firstName, @RequestParam String email){
            return clientRepository.findByFirstNameAndEmailIgnoreCase(firstName,email).stream().map(ClientDTO::new).collect(Collectors.toList());
        }

    @DeleteMapping("/clients/delete/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable Long id){
        this.clientRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/clients") //POST porque crea un cliente con nfo nueva que le envian
    public ResponseEntity<Object> createClient(@RequestParam String firstName, @RequestParam String lastName,
                                               @RequestParam String email, @RequestParam String password) { //estos son los parametros necesarios para crear un client
        //aca controlamos que los datos estén OK

        //si algun campo esta vacio, pasa esto:
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN); //falta data
        }
        //sino, sigue acá:
        //ahora controla si el mail ya existe
        if (clientRepository.findByEmailIgnoreCase(email).isPresent()) {
            return new ResponseEntity<>("UserName already exists", HttpStatus.FORBIDDEN); //prohibido, ya existe
        }

        try {
            //ahora guardo el client y a continuación el account/
            Client client = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

            //acá genero un vn para mi account:
            StringBuilder newNumber = new StringBuilder("VIN");
            for (int i = 0; i < 3; i++) {
                int num = (int) (Math.random() * 10);
                newNumber.append(String.valueOf(num)); //eso equivale a => newNumber += String.valueOf(num)

                Account account; //creo un objeto cuenta
                int accounts = accountRepository.findAll().size(); //creo un entero que me diga qué cantidad de accounts tengo creadas

                do { //hace esto:
                    account = new Account(newNumber.toString(), LocalDateTime.now(), 00.00, client); //crea la cuenta en sí

                } while (accountRepository.findByNumber(account.getNumber()).isPresent()); //si el num de la cuenta existe:

                accountRepository.save(account); //guardo la cuenta
                return new ResponseEntity<>(HttpStatus.CREATED); //devuelve un created
            }
        } catch (Exception ex) { //si falla algo de arriba, devuelve este error
            ex.printStackTrace();
            return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR); //error inesperado
        }

        return new ResponseEntity<>(HttpStatus.CREATED); //SI TODI VA BIEN, DEVUELVE ESTO! (un OK)

    }
}
