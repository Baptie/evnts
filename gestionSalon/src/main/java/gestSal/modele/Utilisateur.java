package gestSal.modele;

import java.util.List;

public class Utilisateur {
    private int idUtilisateur;
    private String pseudo,email,description, status, password;
    private List<Conversation> mesConversations;

    public Utilisateur(int idUtilisateur, String pseudo, String email, String description, String status, String password, List<Conversation> mesConversations) {
        this.idUtilisateur = idUtilisateur;
        this.pseudo = pseudo;
        this.email = email;
        this.description = description;
        this.status = status;
        this.password = password;
        this.mesConversations = mesConversations;
    }

    public Utilisateur() {
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Conversation> getMesConversations() {
        return mesConversations;
    }

    public void setMesConversations(List<Conversation> mesConversations) {
        this.mesConversations = mesConversations;
    }
}
