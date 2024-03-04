package gestSal.apireponses;

import gestSal.modele.Salon;

public class ApiResponseSalon {
    private Salon salon;
    private String message;
    private boolean success;

    public ApiResponseSalon(Salon salon) {
        this.salon = salon;
        this.success = true;
    }

    public ApiResponseSalon(String message) {
        this.message = message;
        this.success = false;
    }

    // Getters et Setters
    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
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
