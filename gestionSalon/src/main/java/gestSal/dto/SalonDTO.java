package gestSal.dto;


import gestSal.bdd.InteractionBDDSalon;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class SalonDTO {
    private int idSalon;
    private int numSalon;
    private String nomSalon,nomCreateur,logo;
    private List<Utilisateur> listeMembre, listeModerateur;
    private List<Message> conversation;
    private List<Evenement> lesEvenements;

    public SalonDTO(int idSalon, int numSalon, String nomSalon, String nomCreateur, String logo, List<Utilisateur> listeMembre, List<Utilisateur> listeModerateur, List<Message> conversation, List<Evenement> lesEvenements) {
        this.idSalon = idSalon;
        this.numSalon = numSalon;
        this.nomSalon = nomSalon;
        this.nomCreateur = nomCreateur;
        this.logo = logo;
        this.listeMembre = listeMembre;
        this.listeModerateur = listeModerateur;
        this.conversation = conversation;
        this.lesEvenements = lesEvenements;
    }

    public SalonDTO() {
    }

    public static void creerSalonSQL(String nomSalon, String nomCreateur, String url) {
        InteractionBDDSalon bdd = new InteractionBDDSalon();
        try {
            bdd.creerSalonSQL(nomSalon,nomCreateur,url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Salon getSalonById(int numSalon) throws SQLException {
        InteractionBDDSalon bdd = new InteractionBDDSalon();
        Salon salon = bdd.getSalonById(numSalon);
        return salon;
    }

    public static Salon modifierSalonSQL(Salon salon,String choix, String valeur, int numSalon) throws SQLException {
        InteractionBDDSalon bdd = new InteractionBDDSalon();
        Salon salonReturn = bdd.modifierSalonSQL(salon,choix,valeur, numSalon);
        return salonReturn;
    }




    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public int getNumSalon() {
        return numSalon;
    }

    public void setNumSalon(int numSalon) {
        this.numSalon = numSalon;
    }

    public String getNomSalon() {
        return nomSalon;
    }

    public void setNomSalon(String nomSalon) {
        this.nomSalon = nomSalon;
    }

    public String getNomCreateur() {
        return nomCreateur;
    }

    public void setNomCreateur(String nomCreateur) {
        this.nomCreateur = nomCreateur;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Utilisateur> getListeMembre() {
        return listeMembre;
    }

    public void setListeMembre(List<Utilisateur> listeMembre) {
        this.listeMembre = listeMembre;
    }

    public List<Utilisateur> getListeModerateur() {
        return listeModerateur;
    }

    public void setListeModerateur(List<Utilisateur> listeModerateur) {
        this.listeModerateur = listeModerateur;
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public void setConversation(List<Message> conversation) {
        this.conversation = conversation;
    }

    public List<Evenement> getLesEvenements() {
        return lesEvenements;
    }

    public void setLesEvenements(List<Evenement> lesEvenements) {
        this.lesEvenements = lesEvenements;
    }
}
