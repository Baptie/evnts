package gestUtil.dto;

import gestUtil.modele.Utilisateur;

import java.util.List;

public class UtilisateurPublicDTO {
    private String pseudo;

    private String bio;

    private String photoDeProfil; // byte[] ???

    private List<String> listeContacts;

    public UtilisateurPublicDTO(String pseudo, String bio, String photoDeProfil, List<String> listeContacts) {
        this.pseudo = pseudo;
        this.bio = bio;
        this.photoDeProfil = photoDeProfil;
        this.listeContacts = listeContacts;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoDeProfil() {
        return photoDeProfil;
    }

    public void setPhotoDeProfil(String photoDeProfil) {
        this.photoDeProfil = photoDeProfil;
    }

    public List<String> getListeContacts() {
        return listeContacts;
    }

    public void setListeContacts(List<String> listeContacts) {
        this.listeContacts = listeContacts;
    }
}
