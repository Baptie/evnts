package auth.facade;

import auth.dto.UtilisateurDTO;
import auth.exception.*;
import auth.modele.Utilisateur;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component("facadeAuth")
public class FacadeAuthentificationImpl implements FacadeAuthentificationInterface {

    private PasswordEncoder passwordEncoder;
    String idConnection;

    public FacadeAuthentificationImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public void inscription(String pseudo, String mdp, String eMail) throws EMailDejaPrisException, EmailOuPseudoDejaPrisException {
        UtilisateurDTO.enregistrerUser(eMail,pseudo,passwordEncoder.encode(mdp));
    }

    @Override
    public String connexion(String pseudo, String mdp){
        idConnection = UUID.randomUUID().toString();
        return idConnection;
    }

    @Override
    public void deconnexion(String pseudo) throws UtilisateurDejaDeconnecteException {
        if(idConnection!=null){
            idConnection =null;
        }else{
            throw new UtilisateurDejaDeconnecteException();
        }
    }

    @Override
    public boolean getStatus(String pseudo)  {
        return false;
    }


    @Override
    public void reSetPseudo(String ancienPseudo, String nouveauPseudo) throws UtilisateurInexistantException {
        UtilisateurDTO.resetPseudo(ancienPseudo,nouveauPseudo);
    }

    @Override
    public void reSetMDP(String pseudo, String mdp, String nouveauMDP) throws UtilisateurInexistantException {
        UtilisateurDTO.resetMDP(pseudo,passwordEncoder.encode(nouveauMDP));
    }

    @Override
    public void supprimerUtilisateur(String pseudo, String mdp) throws UtilisateurInexistantException {
        UtilisateurDTO.supprimerUtilisateur(pseudo);
    }

    @Override
    public Map<String, Utilisateur> getUtilisateurs() {
        return null;
    }

}
