package com.example.demo.Repositories;

import com.example.demo.Dtos.ClientDTO;
import com.example.demo.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByLastNameIgnoreCase(String lastName);

    Optional<Client> findByEmailIgnoreCase(String email);

    List<Client> findByFirstNameIgnoreCase(String firstName);

    Optional<Client> findByFirstNameAndEmailIgnoreCase(String firstName, String email);


}

//    ClientRepository:
//    -Buscar una lista de clientes por nombre
//    -Buscar un cliente por Nombre y Email
//    -Buscar una lista de clientes por apellido