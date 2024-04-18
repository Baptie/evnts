package com.example.gestiongoogle.controleur;

import com.example.gestiongoogle.exceptions.AucunCalendrierTrouveException;
import com.example.gestiongoogle.exceptions.DateFinEvenementInvalideException;
import com.example.gestiongoogle.exceptions.ProblemeEnvoiMailException;
import com.example.gestiongoogle.facade.FacadeGoogleImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.gestiongoogle.service.EmailService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class GoogleControleur {

    @Autowired
    FacadeGoogleImpl facadeGoogle;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    public GoogleControleur(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    private static final Logger logger = LoggerFactory.getLogger(GoogleControleur.class);
    @GetMapping("/")
    public String home() {
        //TODO : pas très propre, mais c'est juste direct pour envoyer vers /login
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><title>Accueil</title></head>" +
                "<body>" +
                "<h1>Ici c'est l'accueil, pas besoin d'être log</h1>" +
                "<form action='/login' method='get'>" +
                "<input type='submit' value='Se connecter'>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/secured")
    public String secured(Authentication authentication, HttpServletRequest request) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        /*
        boolean nouvelUtilisateur = facadeGoogle.verifierUtilisateurExistant();

        if(nouvelUtilisateur){
            return "redirect:/newAccount";
        }*/

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        //String token = oidcUser.getAccessTokenHash();

        // Récupérer le jeton CSRF IMPORTANT : IL FAUT QU'IL FASSE PARTIE DE LA REQUETE !!!!!!
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        return "<!DOCTYPE html>" +
                "<form action='/event' method='post'>" +
                "Titre: <input type='text' name='nom'><br>" +
                "Emplacement: <input type='text' name='location'><br>" +
                "Début: <input type='datetime-local' name='debut'><br>" +
                "Fin: <input type='datetime-local' name='fin'><br>" +
                "Description: <textarea name='description'></textarea><br>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "<input type='submit' value='Ajouter Événement'>" +
                "</form>" +
                "<form action='/emailValidationEvenement' method='get'>" +
                "<input type='submit' value='Envoyer un mail'>" +
                "</form>" +
                "</form>" +
                "<form action='/events' method='post'>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "Début: <input type='datetime-local' name='dateDebut'><br>" +
                "Fin: <input type='datetime-local' name='dateFin'><br>" +
                "<input type='submit' value='Recuperer Les Evenements'>" +
                "</form>" +
                 "</body>" +
                "</html>";
    }

    @GetMapping("/newAccount")
    public String newAccount(Authentication authentication,HttpServletRequest request){
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        // Récupérer le jeton CSRF IMPORTANT : IL FAUT QU'IL FASSE PARTIE DE LA REQUETE !!!!!!
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Création d'Utilisateur</title>" +
                "</head>" +
                "<body>" +
                "<form action='/new-user' method='post'>" +
                "Email: <input type='email' name='email' value="+email+ "readonly required><br>" +
                "Pseudo: <input type='text' name='pseudo' required><br>" +
                "Bio: <textarea name='bio'></textarea><br>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "<input type='submit' value='Créer Utilisateur'>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    @PostMapping("/new-user")
    public ResponseEntity<String> newUser(Authentication authentication,
                                     @RequestParam String email,
                                     @RequestParam String pseudo,
                                     @RequestParam String bio){
        try{
            facadeGoogle.newUtilisateur(email,pseudo,bio);

            return ResponseEntity.ok("Utilisateur ajoute");
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur");
        }

    }

    @PostMapping("/event")
    public ResponseEntity<String> addEvent(Authentication authentication,
                                   @RequestParam String nom,
                                   @RequestParam String location,
                                   @RequestParam String debut,
                                   @RequestParam String fin,
                                   @RequestParam String description) throws GeneralSecurityException, IOException, AucunCalendrierTrouveException {
        try {
            facadeGoogle.ajouterEvenementCalendrier(authentication, nom, location, debut, fin, description);

            return ResponseEntity.ok("Evenement ajoute");
        }catch(AucunCalendrierTrouveException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Aucun google calendar trouve");
        } catch (DateFinEvenementInvalideException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Date debut et fin invalide");
        }

    }
    @PostMapping("/events")
    public ResponseEntity<String> getEvents(Authentication authentication,
                                            @RequestParam String dateDebut,
                                            @RequestParam String dateFin){
        try{
            List<Event> listeEvenement = facadeGoogle.listerEvenements(authentication,dateDebut,dateFin);

            return ResponseEntity.ok(listeEvenement.toString());
        } catch (GeneralSecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Probleme Recuperation Evenements Securite");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Probleme Recuperation Evenements");
        }
    }

    @GetMapping("/emailValidationEvenement")
    public ResponseEntity<String> sendEmail(Authentication authentication) throws ProblemeEnvoiMailException {
        try{
            facadeGoogle.envoyerMailValidationEvenement(authentication);

            return ResponseEntity.ok("Mail Envoye");
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Probleme envoi mail");
        }

    }

    @GetMapping("/email")
    public ResponseEntity<String> sendEmailAvecContenu(String email, String objet, String contenu)throws ProblemeEnvoiMailException{
        try{
            facadeGoogle.envoyerMailParContenu(email,objet,contenu);

            return ResponseEntity.ok("Mail Envoue");
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Probleme envoi mail");
        }
    }




}
