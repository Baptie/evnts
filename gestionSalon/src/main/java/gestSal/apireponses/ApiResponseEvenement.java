package gestSal.apireponses;

import gestSal.dto.EvenementDTO;
import gestSal.modele.Evenement;

public class ApiResponseEvenement {
    private EvenementDTO evenement;
    private String message;
    private boolean success;

    public ApiResponseEvenement(EvenementDTO evenement) {
        this.evenement = evenement;
        this.success = true;
    }

    public ApiResponseEvenement(String message) {
        this.message = message;
        this.success = false;
    }

    public EvenementDTO getEvenement() {
        return evenement;
    }

    public void setEvenement(EvenementDTO evenement) {
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
