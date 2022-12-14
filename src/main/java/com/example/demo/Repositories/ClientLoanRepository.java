package com.example.demo.Repositories;

import com.example.demo.Models.Client;
import com.example.demo.Models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

    List<ClientLoan> findByClient(Client  client);

    List<ClientLoan> findByAmountGreaterThan (Double amount);

    List<ClientLoan> findByClientAndAmountLessThan(Client client, Double amount);

}





//    ClientLoanRepository:
//    -Buscar una lista de ClientLoan por cliente
//    -Buscar una lista de ClientLoan que sean mayores a x monto pasado por parametro
//    -Buscar una lista de ClientLoan por cliente que  en cual sus balances sean menores a x monto pasado por parametro