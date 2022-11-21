package com.example.demo.Repositories;

import com.example.demo.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(String number);

    List<Account> findByBalanceGreaterThan(double balance);

    List<Account> findByCreationDateBefore(LocalDateTime creationDate);

}


//    AccountRepository:
//    -Buscar una lista de cuentas en el cual su balance se mayor a x monto pasado por parametro
//    -Buscar una lista de cuentas por en la cual sue fecha se menor a la que le pasemos por parametro
//    -Buscar una cuenta por Numero de cuenta