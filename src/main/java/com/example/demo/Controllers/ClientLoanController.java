package com.example.demo.Controllers;

import com.example.demo.Dtos.ClientLoanDTO;
import com.example.demo.Models.Client;
import com.example.demo.Models.ClientLoan;
import com.example.demo.Repositories.ClientLoanRepository;
import com.example.demo.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ClientLoanController {

    @Autowired
    private ClientLoanRepository clientloanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clientloans")
    public List<ClientLoanDTO> getClients() {
        return clientloanRepository.findAll().stream().map(ClientLoanDTO::new).collect(toList());
    }

    @GetMapping("/clientloans/{id}")
    public ClientLoanDTO getLoan(@PathVariable Long id) {
        Optional<ClientLoan> clientOptional = clientloanRepository.findById(id);
        return clientloanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    }

    //-------lista clientloan por Cliente
    @GetMapping("/clientLoans/client/{client}")
    public List<ClientLoanDTO> getClientLoansByClient(@PathVariable Client client) {
        return clientloanRepository.findByClient(client).stream().map(ClientLoanDTO::new).collect(toList());
    }

    //--------clientloan por amount mayor al pasado---------
    @GetMapping("/clientloans/amountGreater/{amount}")
    public List<ClientLoanDTO> clientLoan(@PathVariable Double amount) {
        return clientloanRepository.findByAmountGreaterThan(amount).stream().map(ClientLoanDTO::new).collect(toList());
    }
}
