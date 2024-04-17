package auth.dto;

import auth.exception.EMailDejaPrisException;
import bdd.InteractionBDDAuthentification;

import java.sql.SQLException;

public class UtilisateurDTO {
    private int id;
    private String pseudo,mdp,email;
//
//    public UtilisateurDTO(int id, String pseudo, String mdp, String email) {
//        this.id = id;
//        this.pseudo = pseudo;
//        this.mdp = mdp;
//        this.email = email;
//    }
//
//    public UtilisateurDTO() {
//    }


    public static void enregistrerUser(String email, String pseudo, String mdp) throws EMailDejaPrisException {
        InteractionBDDAuthentification bdd = new InteractionBDDAuthentification();
        try {
            bdd.enregistrerUser(email,pseudo,mdp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetPseudo(String ancienPseudo, String nouveauPseudo) {
        InteractionBDDAuthentification bdd = new InteractionBDDAuthentification();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
