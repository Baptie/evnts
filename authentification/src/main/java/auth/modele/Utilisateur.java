package auth.modele;

public class Utilisateur {

    private final String pseudo;

    private final String eMail;

    private final String mdp;

    public Utilisateur(String pseudo, String eMail, String mdp) {
        this.pseudo = pseudo;
        this.eMail = eMail;
        this.mdp = mdp;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEMail() {
        return eMail;
    }

    public String getMdp() {
        return mdp;
    }
}
