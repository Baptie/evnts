package gestSal.dto;

import gestSal.bdd.InteractionBDDSalon;
import gestSal.facade.erreurs.EvenementIncompletException;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import java.sql.SQLException;
import java.util.List;

public class EvenementDTO {
    private int nombrePersonneMax;
    private String nomEvenement,detailsEvenement,lieu,nomCreateur;
    private String date;
    private boolean estTermine,estValide;


    public EvenementDTO() {
    }

    public static Evenement getEvenementByNomEtNumSalon(int numSalon, String nomEvenement) throws SQLException {
        return InteractionBDDSalon.getEvenementByNomEtNumSalonSQL(numSalon,nomEvenement);
    }

    public static void modifierEvenement(Evenement evenement, String choix, String valeur) throws SQLException {
        InteractionBDDSalon.modifierEvenementSQL(evenement,choix,valeur);
    }

    public static void validerEvenement(Evenement evenement) throws SQLException, EvenementIncompletException {
        InteractionBDDSalon.validerEvenementSQL(evenement);
    }

    public static List<Message> getMessagesEvenement(Evenement evenement) throws SQLException {
        return InteractionBDDSalon.getMessageEventSQL(evenement.getIdEvenement());
    }

    public int getNombrePersonneMax() {
        return nombrePersonneMax;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public String getDetailsEvenement() {
        return detailsEvenement;
    }


    public String getLieu() {
        return lieu;
    }

    public String getNomCreateur() {
        return nomCreateur;
    }

    public String getDate() {
        return date;
    }



}

