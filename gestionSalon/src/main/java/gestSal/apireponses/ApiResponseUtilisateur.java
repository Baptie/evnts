package gestSal.apireponses;

import gestSal.modele.Utilisateur;

public class ApiResponseUtilisateur {
    private Utilisateur utilisateur;
    private String message;
    private boolean success;

    public ApiResponseUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.success = true;
    }

    public ApiResponseUtilisateur(String message) {
        this.message = message;
        this.success = false;
    }

    // Getters et Setters
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

