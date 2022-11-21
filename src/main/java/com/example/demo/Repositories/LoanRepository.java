package com.example.demo.Repositories;

import com.example.demo.Models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
