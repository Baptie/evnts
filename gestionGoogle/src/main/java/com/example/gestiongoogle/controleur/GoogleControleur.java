package com.example.gestiongoogle.controleur;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleControleur {
    @GetMapping("/")
    public String home(){return "Ici c'est l'accueil, pas besoin d'etre log";}

    @GetMapping("/secured")
    public String secured(){return "bien connecté et pas accessible si t'es pas co";}

}
