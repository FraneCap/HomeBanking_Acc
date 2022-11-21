package com.example.demo.Controllers;


import com.example.demo.Dtos.CardDTO;
import com.example.demo.Models.Card;
import com.example.demo.Models.CardColor;
import com.example.demo.Models.CardType;
import com.example.demo.Models.Client;
import com.example.demo.Repositories.CardRepository;
import com.example.demo.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients/{id}/cards")
    public List<CardDTO> getCards(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get().getCards().stream().map(CardDTO::new).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }

    }

    @PostMapping("/clients/current/cards") //
    public ResponseEntity<Object> createCards(Authentication authentication, @RequestParam CardType cardType,
                                              @RequestParam CardColor cardColor) {
        try {

            Optional<Client> clientOptional = clientRepository.findByEmailIgnoreCase(authentication.getName());

            if (clientOptional.isPresent()) { //si existe el client:

                if (clientOptional.get().getCards().stream().filter(card -> card.getType() == cardType).count() < 3) {
                    cardRepository.save(new Card(cardType, cardColor, clientOptional.get()));
                    return new ResponseEntity<>(HttpStatus.CREATED); //devuelve un created

                } else {
                    return new ResponseEntity<>("You already have 3 " + cardType + " cards", HttpStatus.INTERNAL_SERVER_ERROR); //avisa que ya no hay cuentas disponbles
                }

            } else {
                return new ResponseEntity<>("Cliente inexistente! ", HttpStatus.INTERNAL_SERVER_ERROR); //avisa que ya no hay cuentas disponbles
            }



        } catch (Exception ex) { //si falla algo de arriba, devuelve este error
            ex.printStackTrace();
            return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR); //error inesperado
        }
    }
}





//-------------------------EXTRA

    /* ESTA ES OTRA FORMA MAS LARGA DE HACER EL CONTEO DE TARJETAS DE CADA TIPO /

                int contador = 0;
                for (Card card : clientOptional.get().getCards()) {
                    if (card.getType() == cardType) {
                        contador++;
                }*/



    /* Esta es una alternativa usando lambda
    import org.springframework.security.core.Authentication;
@PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor,
                                             Authentication authentication){
        Optional<Client> client = this.clientRepository.findByEmail(authentication.getName());
if (client.get().getCards().stream().filter(card -> card.getType() == cardType).count() >= 3) {
                return new ResponseEntity<>("You already have 3 " + cardType + " cards", HttpStatus.FORBIDDEN);
            }
     */



/*  DO WHILE

do {


                if (clientOptional.get().getCards().isEmpty()) {
                    cardRepository.save(new Card(cardType, cardColor, clientOptional.get()));
                    return new ResponseEntity<>(HttpStatus.CREATED); //devuelve un created
                }

            } while (clientOptional.get().getCards().stream().filter(card -> card.getType() == cardType).count() > 2);
            return new ResponseEntity<>("No puede crear màs de 3 tarjetas del mismo tipo. ", HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            return new ResponseEntity<>("Cliente inexistente! ", HttpStatus.INTERNAL_SERVER_ERROR);



 */

