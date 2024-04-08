package com.example.gestiongoogle.facade;

import com.example.gestiongoogle.exceptions.AucunCalendrierTrouveException;
import com.example.gestiongoogle.exceptions.DateFinEvenementInvalideException;
import com.example.gestiongoogle.exceptions.ProblemeEnvoiMailException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IFacadeGoogle {
    /**
     *
     * @param authentication
     * @param nom
     * @param location
     * @param debut
     * @param fin
     * @param description
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws AucunCalendrierTrouveException
     * @throws DateFinEvenementInvalideException
     */
    String ajouterEvenementCalendrier(Authentication authentication, String nom, String location, String debut, String fin, String description) throws IOException, GeneralSecurityException, AucunCalendrierTrouveException, DateFinEvenementInvalideException;

    void envoyerMailValidationEvenement(Authentication authentication) throws ProblemeEnvoiMailException;
}
