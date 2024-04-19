package auth.dto;

import auth.exception.EMailDejaPrisException;
import auth.exception.EmailOuPseudoDejaPrisException;
import auth.exception.UtilisateurInexistantException;
import bdd.InteractionBDDAuthentification;

import java.sql.SQLException;

public class UtilisateurDTO {
    private int id;
    private String pseudo,mdp,email;



    public static void enregistrerUser(String email, String pseudo, String mdp) throws EMailDejaPrisException, EmailOuPseudoDejaPrisException {
        InteractionBDDAuthentification bdd = new InteractionBDDAuthentification();
        try {
            bdd.enregistrerUtilisateur(email,pseudo,mdp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetPseudo(String ancienPseudo, String nouveauPseudo) throws UtilisateurInexistantException {
        InteractionBDDAuthentification bdd = new InteractionBDDAuthentification();
        try {
            bdd.resetPseudo(ancienPseudo,nouveauPseudo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void resetMDP(String pseudo, String nouveauMDP) {
        InteractionBDDAuthentification bdd = new InteractionBDDAuthentification();
        try {
            bdd.resetMDP(pseudo, nouveauMDP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void supprimerUtilisateur(String pseudo) {
        InteractionBDDAuthentification bdd = new InteractionBDDAuthentification();
        try {
            bdd.supprimerUtilisateur(pseudo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
