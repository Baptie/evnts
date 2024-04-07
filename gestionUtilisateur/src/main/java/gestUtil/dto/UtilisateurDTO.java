package gestUtil.dto;

import java.util.ArrayList;
import java.util.List;

//@AllArgsConstructor
//@Entity
public class UtilisateurDTO  {
    //@Id
    private String email;
    private String pseudo;

    private int id;

    private String bio,status;

    private String photoDeProfil; // byte[] ???

    private ArrayList<String> listeContact;


    public UtilisateurDTO(int id, String email, String pseudo, String bio, String photoDeProfil, String status, ArrayList<String>listeContact) {
        this.id=id;
        this.email = email;
        this.pseudo = pseudo;
        this.bio = bio;
        this.photoDeProfil = photoDeProfil;
        this.status = status;
        this.listeContact = listeContact;
    }

    public UtilisateurDTO() {
    }

    public ArrayList<String> getListeContact() {
        return listeContact;
    }

    public void setListeContact(ArrayList<String> listeContact) {
        this.listeContact = listeContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }



    public String getPseudo() {
        return pseudo;
    }

    public String getBio() {
        return bio;
    }

    public String getPhotoDeProfil() {
        return photoDeProfil;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPhotoDeProfil(String photoDeProfil) {
        this.photoDeProfil = photoDeProfil;
    }


}
