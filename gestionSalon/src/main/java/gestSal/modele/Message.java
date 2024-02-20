package gestSal.modele;

import java.util.Date;

public class Message {
    private int idMessage;
    private String auteur,receveur,contenu;
    private Date date;
    private boolean isSeen;

    public Message(int idMessage, String auteur, String receveur, String contenu, Date date, boolean isSeen) {
        this.idMessage = idMessage;
        this.auteur = auteur;
        this.receveur = receveur;
        this.contenu = contenu;
        this.date = date;
        this.isSeen = isSeen;
    }

    public Message() {
    }

    public int getIdMessage() {
        return idMessage;
    }


    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getReceveur() {
        return receveur;
    }

    public void setReceveur(String receveur) {
        this.receveur = receveur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
