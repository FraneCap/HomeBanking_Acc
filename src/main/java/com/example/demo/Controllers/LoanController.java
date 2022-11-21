package com.example.demo.Controllers;

import com.example.demo.Dtos.ClientLoanDTO;
import com.example.demo.Dtos.LoanApplicationDTO;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {


    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<ClientLoanDTO> getClientsLoans() {
        return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(toList());
    }


    @GetMapping("/clients/{id}/loans")
    public ClientLoanDTO getClientLoans(@PathVariable Long id) {
        return clientLoanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    }

    @Transactional
    @PostMapping("/loans")

    public ResponseEntity<Object> createClientLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        try {
            Client client = clientRepository.findByEmailIgnoreCase(authentication.getName()).get();
            Optional<Loan> loan = loanRepository.findById(loanApplicationDTO.getLoanId());
            Optional<Account> account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
            //verificar que el prestamo exista
            if (loan.isEmpty()) {
                return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
            }
            //verificar que la cuenta exista

            if (account.isEmpty()) {
                return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
            }
            //Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
            if (loanApplicationDTO.getLoanId() == 0 || loanApplicationDTO.getToAccountNumber().isEmpty() || loanApplicationDTO.getAmount()==0||
                    loanApplicationDTO.getPayments() == 0) {
                return new ResponseEntity<>("empty parameters ", HttpStatus.FORBIDDEN);
            }
            //Verificar que el monto solicitado no exceda el monto máximo del préstamo
            if (loanApplicationDTO.getAmount() > loan.get().getMaxAmount()) {
                return new ResponseEntity<>("exceeds max amount", HttpStatus.FORBIDDEN);
            }
            //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
            if (!loan.get().getPayments().contains(loanApplicationDTO.getPayments())) {
                return new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
            }
//        //Verificar que la cuenta de destino pertenezca al cliente autenticado
            if (!authentication.getName().equals(account.get().getClient().getEmail())) {
                return new ResponseEntity<>("Not authorized", HttpStatus.FORBIDDEN);
            }
            //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
            Double loanInterest = ((loanApplicationDTO.getAmount() * 0.20) + loanApplicationDTO.getAmount());

            ///Debe recibir un objeto de solicitud de crédito con los datos del préstamo
            Transaction transaction = new Transaction(account.get(), TransactionType.CREDIT, loanApplicationDTO.getAmount(),  loan.get().getName() + " loan approved", LocalDateTime.now());
            transactionRepository.save(transaction);  //account , transaction type, double, string, localdatetime  (account.get(), TransactionType.CREDIT, loanApplicationDTO.getAmount(),  loan.get().getName() + " loan approved", LocalDateTime.now()
            ClientLoan clientLoan = new ClientLoan(loanInterest, loanApplicationDTO.getPayments(), client, loan.get());
            clientLoanRepository.save(clientLoan);
            return new ResponseEntity<>(" loan approved", HttpStatus.CREATED);

        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<>("Unexpected", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody Loan loan) {
        Client client = clientRepository.findByEmailIgnoreCase(authentication.getName()).get();
        if (!client.getEmail().contains("@admin")) {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }
        Loan loan1 = new Loan(loan.getName(), loan.getMaxAmount(), loan.getPayments());
        loanRepository.save(loan1);
        return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
    }
}




//estructura JSON
//      {
//         "name": "ipotecario",
//        "maxAmount": 50.0,
//        "payments": [
//            2,
//            4
//            ]
//        }









//Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
//Se debe actualizar la cuenta de destino sumando el monto solicitado.





