package gestSal.dto;

import bdd.InteractionBDDSalon;
import gestSal.modele.Conversation;
import gestSal.modele.Evenement;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class UtilisateurDTO {
    private int idUtilisateur;
    private String pseudo,email,description, status, password;
    private List<Conversation> mesConversations;

    public UtilisateurDTO() {
    }

    public static Utilisateur getUtilisateurByPseudo(String pseudoUtilisateur) throws SQLException {
        return InteractionBDDSalon.getUtilisateurByPseudoSQL(pseudoUtilisateur);
    }

    public static List<Utilisateur> seDefiniCommePresentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws SQLException {
        List<Utilisateur> lesParticipants;
        lesParticipants = InteractionBDDSalon.seDefiniCommePresentAUnEvenementSQL(utilisateur,evenement);
        return lesParticipants;
    }

    public static List<Utilisateur> seDefiniCommeAbsentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws SQLException {
        List<Utilisateur> lesParticipants ;
        lesParticipants = InteractionBDDSalon.seDefiniCommeAbsentAUnEvenementSQL(utilisateur,evenement);
        return lesParticipants;
    }

    public static void supprimerUtilisateur(Utilisateur pseudoUtilisateur) throws SQLException {
        InteractionBDDSalon.supprimerUtilisateurSQL(pseudoUtilisateur);
    }

    public static List<Integer> getSalonByUser( int idUtilisateur) throws SQLException {
        return InteractionBDDSalon.getSalonByUser(idUtilisateur);

    }

    public static List<Integer> getEventByUser(int idUser) throws SQLException {
        return InteractionBDDSalon.getEventByUser(idUser);

    }

    public static Evenement getEventById(int idEvenement) throws SQLException {
        return InteractionBDDSalon.getEventById(idEvenement);
    }

    public static void ajouterMembre(String nomMembre) throws SQLException {
        InteractionBDDSalon.ajouterMembre(nomMembre);
    }


    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public List<Conversation> getMesConversations() {
        return mesConversations;
    }

}
