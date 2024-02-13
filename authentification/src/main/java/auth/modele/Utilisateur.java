package auth.modele;

public class Utilisateur {

    private final String pseudo;

    private final String eMail;

    private final String mdp;

    private boolean status;

    public Utilisateur(String pseudo, String eMail, String mdp) {
        this.pseudo = pseudo;
        this.eMail = eMail;
        this.mdp = mdp;
        this.status = false;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
