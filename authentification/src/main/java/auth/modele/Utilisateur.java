package auth.modele;

public class Utilisateur {

    private String pseudo;

    private final String eMail;

    private String mdp;

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

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
