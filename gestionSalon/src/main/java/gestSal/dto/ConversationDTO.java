package gestSal.dto;

import gestSal.modele.Conversation;
import gestSal.modele.Message;

import java.util.List;

public class ConversationDTO {
    private int idConversation;
    private String utilisateurUn, utilisateurDeux;
    private List<Message> lesMessagesDeLaConversation;

    public ConversationDTO(int idConversation, String utilisateurUn, String utilisateurDeux, List<Message> lesMessagesDeLaConversation) {
        this.idConversation = idConversation;
        this.utilisateurUn = utilisateurUn;
        this.utilisateurDeux = utilisateurDeux;
        this.lesMessagesDeLaConversation = lesMessagesDeLaConversation;
    }

    public ConversationDTO() {
    }


    public int getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(int idConversation) {
        this.idConversation = idConversation;
    }

    public String getUtilisateurUn() {
        return utilisateurUn;
    }

    public void setUtilisateurUn(String utilisateurUn) {
        this.utilisateurUn = utilisateurUn;
    }

    public String getUtilisateurDeux() {
        return utilisateurDeux;
    }

    public void setUtilisateurDeux(String utilisateurDeux) {
        this.utilisateurDeux = utilisateurDeux;
    }

    public List<Message> getLesMessagesDeLaConversation() {
        return lesMessagesDeLaConversation;
    }

    public void setLesMessagesDeLaConversation(List<Message> lesMessagesDeLaConversation) {
        this.lesMessagesDeLaConversation = lesMessagesDeLaConversation;
    }
}

