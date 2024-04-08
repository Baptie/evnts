package com.example.gestiongoogle.facade;

import com.example.gestiongoogle.exceptions.AucunCalendrierTrouveException;
import com.example.gestiongoogle.exceptions.DateFinEvenementInvalideException;
import com.example.gestiongoogle.exceptions.ProblemeEnvoiMailException;
import com.example.gestiongoogle.service.EmailService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("facadeGoogle")
public class FacadeGoogleImpl implements IFacadeGoogle {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Autowired
    private EmailService emailService;

    public String ajouterEvenementCalendrier(Authentication authentication, String nom, String location, String debut, String fin, String description) throws IOException, GeneralSecurityException, AucunCalendrierTrouveException, DateFinEvenementInvalideException {

        Event event = new Event()
                .setSummary(nom)
                .setLocation(location)
                .setDescription(description);

        //on convertit le string en formulaire
        DateTime startDateTime = new DateTime(debut+":00.000Z"); // Ajout de secondes et fuseau horaire UTC
        DateTime endDateTime = new DateTime(fin+":00.000Z");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Conversion des chaînes en LocalDateTime
        LocalDateTime startDateTimeVerif = LocalDateTime.parse(debut, formatter);
        LocalDateTime endDateTimeVerif = LocalDateTime.parse(fin, formatter);

        // Vérification que endDateTime n'est pas avant startDateTime
        if (endDateTimeVerif.isBefore(startDateTimeVerif)) {
            throw new DateFinEvenementInvalideException();
        }


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
            throw new AucunCalendrierTrouveException();
        }
        // Ajouter l'événement au calendrier choisi
        event = service.events().insert(calendarId, event).execute();
        return "Événement ajouté : " + event.getHtmlLink();

    }

    public void envoyerMailValidationEvenement(Authentication authentication) throws ProblemeEnvoiMailException {
        try{
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

            String email = oidcUser.getEmail();

            emailService.sendSimpleMessage(email, "Evenement bien créé", "Bonjour, \n Nous vous confirmons la bonne création de votre evenement.\nA bientot!");
        }catch(Exception e){
            throw new ProblemeEnvoiMailException();
        }
    }
}
