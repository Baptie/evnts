package gestSal.apireponses;

import gestSal.modele.Evenement;

public class ApiResponseEvenement {
    private Evenement evenement;
    private String message;
    private boolean success;

    public ApiResponseEvenement(Evenement evenement) {
        this.evenement = evenement;
        this.success = true;
    }

    public ApiResponseEvenement(String message) {
        this.message = message;
        this.success = false;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
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
