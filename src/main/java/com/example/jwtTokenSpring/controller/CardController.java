package com.example.jwtTokenSpring.controller;

import com.example.jwtTokenSpring.entity.Cards;
import com.example.jwtTokenSpring.repository.CardsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {

    private final CardsRepository cardsRepository;

    public CardController(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @GetMapping("/myCards")
    public List<Cards> getCardDetails(@RequestParam long id) {
        List<Cards> cards = cardsRepository.findByCustomerId(id);
        if (cards != null ) {
            return cards;
        }else {
            return null;
        }
    }
}
