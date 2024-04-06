package gestSal.apireponses;

import gestSal.dto.SalonDTO;

public class ApiResponseSalonDTO {
    private SalonDTO salon;
    private String message;
    private boolean success;

    public ApiResponseSalonDTO(SalonDTO salon) {
        this.salon = salon;
        this.success = true;
    }

    public ApiResponseSalonDTO(String message) {
        this.message = message;
        this.success = false;
    }

    // Getters et Setters
    public SalonDTO getSalon() {
        return salon;
    }

    public void setSalon(SalonDTO salon) {
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
