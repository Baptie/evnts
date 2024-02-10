package gestSal.facade;

import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.Date;
import java.util.List;
import java.util.Random;

@Component("facadeSalon")
public class FacadeSalonImpl implements FacadeSalon {


    @Override
    public Salon creerSalon(String nomCreateur, String nomSalon) throws NomSalonVideException, NomTropCourtException, NumSalonDejaExistantException {
        Salon salonCree = null;
        if(nomSalon.isBlank()){
            throw new NomSalonVideException();
        }else if(nomSalon.length()<=3){
            throw new NomTropCourtException();
        }else{
            int id = generateRandom4DigitNumber();
            salonCree = new Salon();
            salonCree.setNomSalon(nomSalon);
            salonCree.setNomCreateur(nomCreateur);
            salonCree.setNumSalon(id);
        }
        return salonCree;
    }


    @Override
    public Salon modifierSalon(Salon salon, String choix, String valeur) throws SalonInexistantException, NomSalonVideException, NumeroSalonVideException {
        switch (choix) {
            case "num" -> salon.setNumSalon(Integer.parseInt(valeur));
            case "nom" -> salon.setNomSalon(valeur);
            case "logo" -> salon.setLogo(valeur);
            case "createur" -> salon.setNomCreateur(valeur);
        }
        return salon;
    }

    @Override
    public void rejoindreSalon(Utilisateur utilisateur, Salon salonRejoint) throws SalonInexistantException, NomSalonVideException, UtilisateurInexistantException, NomUtilisateurVideException,UtilisateurDejaPresentException {
        if(!salonRejoint.getListeMembre().contains(utilisateur)){
            salonRejoint.getListeMembre().add(utilisateur);
        }else{
            throw new UtilisateurDejaPresentException();
        }
    }

    @Override
    public String inviterUtilisateur(Salon salonInvite, Utilisateur utilisateurInvite) throws SalonInexistantException, UtilisateurInexistantException, NomSalonVideException, NomUtilisateurVideException {

        String baseUrl = "https://evnt.com/invitation?code=";
        String invitationCode = generateRandomCode(8); // Vous pouvez ajuster la longueur du code ici
        String invitationUrl = baseUrl + invitationCode;
        //TODO ENVOIE DE LA NOTIF A LA PERSONNE, MAIL ?
        return invitationUrl;

    }

    @Override
    public Utilisateur getUtilisateurByPseudo(String pseudoUtilisateur) throws NomUtilisateurVideException {
        //TODO
        return null;
    }

    @Override
    public Salon getSalonByNum(int numSalon) throws NumeroSalonVideException {
        //TODO
        return null;
    }

    @Override
    public void retirerModerateurDuSalon(Salon salon, Utilisateur utilisateurPlusModo) throws NomSalonVideException, NomUtilisateurVideException,UtilisateurPasModoException {
        if(salon.getListeModerateur().contains(utilisateurPlusModo)){
            salon.getListeModerateur().remove(utilisateurPlusModo);
        }else{
            throw new UtilisateurPasModoException();
        }
    }

    @Override
    public void ajouterModerateurAuSalon(Utilisateur nouveauModo, Salon salonPourLeNouveauModo) throws NomUtilisateurVideException, NomSalonVideException, PasAdminException,UtilisateurDejaModoException {
        if(!salonPourLeNouveauModo.getListeModerateur().contains(nouveauModo)){
            salonPourLeNouveauModo.getListeModerateur().add(nouveauModo);
        }else{
            throw new UtilisateurDejaModoException();
        }
    }

    @Override
    public List<Utilisateur> seDefiniCommePresentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws UtilisateurInexistantException, SalonInexistantException, EvenementInexistantException {
        List<Evenement> lesEvenements = salon.getLesEvenements();
        if(lesEvenements.contains(evenement)){
            evenement.getListeParticipants().add(utilisateur);
        }
        return evenement.getListeParticipants();
    }

    @Override
    public List<Utilisateur> seDefiniCommeAbsentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws UtilisateurInexistantException, SalonInexistantException, EvenementInexistantException {
        List<Evenement> lesEvenements = salon.getLesEvenements();
        if(lesEvenements.contains(evenement)){
            if(evenement.getListeParticipants().contains(utilisateur)){
                evenement.getListeParticipants().remove(utilisateur);
            }else{
                throw new UtilisateurInexistantException();
            }
        }
        return evenement.getListeParticipants();
    }

    @Override
    public Evenement getEvenementByNomEtNumSalon(int numSalon, String nomEvenement) {
        return null;
    }

    @Override
    public Evenement creerEvenement(String nomEvenement) throws NomEvenementDejaPrisException, NomEvenementVideException {
        Evenement event = null;
        if(nomEvenement.isBlank()){
            throw new NomEvenementVideException();
        }else{
            if(checkSiNomEvenementDejaPris()){
                throw new NomEvenementVideException();
            }else{
                event = new Evenement();
                event.setNomCreateur(nomEvenement);
            }
        }
        return event;
    }

    private boolean checkSiNomEvenementDejaPris() {
        return true;
    }

    @Override
    public Evenement modifierEvenement(Evenement evenement, String choix, String valeur) throws EvenementInexistantException {
        switch (choix) {
            case "description" -> evenement.setDetailsEvenement(valeur);
            case "date" -> evenement.setDate(Date.valueOf(valeur));
            case "lieu" -> evenement.setLieu(valeur);
            case "nombre" -> evenement.setNombrePersonneMax(Integer.parseInt(valeur));
            case "nom" -> evenement.setNomEvenement(valeur);
        }
        return evenement;
    }

    @Override
    public boolean validerEvenement(Evenement evenement) throws EvenementInexistantException {
        boolean isValide;
        if(evenement.getListeParticipants().size()==evenement.getNombrePersonneMax()){
            evenement.setEstValide(true);
            isValide = true;
        }else{
            evenement.setEstValide(false);
            isValide = false;
        }
        return isValide;
    }

    private String generateRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomValue = secureRandom.nextInt('Z' - 'A' + 1) + 'A';
            randomCode.append((char) randomValue);
        }

        return randomCode.toString();
    }
    private int generateRandom4DigitNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
