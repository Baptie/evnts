package gestSal.modele;


import java.util.List;

public class Salon {
    private int idSalon;
    private int numSalon;
    private String nomSalon,nomCreateur,logo;
    private List<Utilisateur> listeMembre, listeModerateur;
    private List<Message> conversation;
    private Evenement evenement;

    public Salon(int idSalon, int numSalon, String nomSalon, String nomCreateur, String logo, List<Utilisateur> listeMembre, List<Utilisateur> listeModerateur, List<Message> conversation, Evenement evenement) {
        this.idSalon = idSalon;
        this.numSalon = numSalon;
        this.nomSalon = nomSalon;
        this.nomCreateur = nomCreateur;
        this.logo = logo;
        this.listeMembre = listeMembre;
        this.listeModerateur = listeModerateur;
        this.conversation = conversation;
        this.evenement = evenement;
    }

    public Salon() {
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

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }
}
