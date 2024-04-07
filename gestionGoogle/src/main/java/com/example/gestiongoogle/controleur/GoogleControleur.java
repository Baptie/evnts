package com.example.gestiongoogle.controleur;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RestController
public class GoogleControleur {

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

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        //String token = oidcUser.getAccessTokenHash();

        // Récupérer le jeton CSRF
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String token = csrfToken.getToken();

        return "<!DOCTYPE html>" +
                // ...
                "<form action='/add-event' method='post'>" +
                "Titre: <input type='text' name='summary'><br>" +
                "Emplacement: <input type='text' name='location'><br>" +
                "Début: <input type='datetime-local' name='debut'><br>" +
                "Fin: <input type='datetime-local' name='fin'><br>" +
                "Description: <textarea name='description'></textarea><br>" +
                "<input type='hidden' name='" + csrfToken.getParameterName() + "' value='" + token + "' />" +
                "<input type='submit' value='Ajouter Événement'>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    @PostMapping("/add-event")
    public String addEvent(Authentication authentication,
                           @RequestParam String summary,
                           @RequestParam String location,
                           @RequestParam String debut,
                           @RequestParam String fin,
                           @RequestParam String description) throws GeneralSecurityException, IOException {
        Event event = new Event()
                .setSummary(summary)
                .setLocation(location)
                .setDescription(description);

        //on convertit le string en formulaire
        DateTime startDateTime = new DateTime(debut+":00.000Z"); // Ajout de secondes et fuseau horaire UTC
        DateTime endDateTime = new DateTime(fin+":00.000Z");

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Paris");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Paris");
        event.setEnd(end);
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        // Récupérer le token d'accès OAuth2
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();

        // Configurer le client de l'API Google Calendar
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Calendar service = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Evnts") // Nom de votre application
                .build();
        // Récupérer la liste des calendriers de l'utilisateur
        String calendarId = null;
        CalendarList calendarList = service.calendarList().list().execute();
        for (CalendarListEntry calendarListEntry : calendarList.getItems()) {
            //on tente de recuperer le calendrier principal
            if (calendarListEntry.getPrimary() != null && calendarListEntry.getPrimary()) {
                calendarId = calendarListEntry.getId();
                break;
            }
        }
        if (calendarId == null) {
            // Gérer l'absence d'un calendrier correspondant
            return "Aucun calendrier approprié trouvé";
        }
        // Ajouter l'événement au calendrier choisi
        event = service.events().insert(calendarId, event).execute();

        return "Événement ajouté : " + event.getHtmlLink();
    }



}
