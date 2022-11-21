package com.example.demo.Repositories;


import com.example.demo.Models.Transaction;
import com.example.demo.Models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCreationDateBetween(LocalDateTime date, LocalDateTime date2);

    List<Transaction> findByAmountBetween(double amount1, double amount2);

    List<Transaction> findByType(TransactionType type);


}


//    TransactionRepository:
//    -Buscar una lista de transacciones entre dos fechas pasadas por parametro
//    -Buscar una lista de transacciones en las cuales el monto se mayor a x monto pasado como primer parametro,
//     y menor a x monto  pasado como segundo parametro
//    -Buscar una lista de transacciones por type



