package auth.facade;

import auth.exception.*;
import auth.modele.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component("facadeAuth")
public class FacadeAuthentificationImpl implements FacadeAuthentificationInterface {

    private Map<String, Utilisateur> utilisateurs;

    private Map<String, Utilisateur> utilisateursConnectes;

    @Override
    public void inscription(String pseudo, String mdp, String eMail) throws PseudoDejaPrisException, EMailDejaPrisException {

        if (utilisateurs.containsKey(pseudo))
            throw new PseudoDejaPrisException();

        if (utilisateurs.containsKey(eMail))
            throw new EMailDejaPrisException();

        this.utilisateurs.put(pseudo,new Utilisateur(pseudo,mdp,eMail));

    }

    @Override
    public String connexion(String pseudo, String mdp) throws UtilisateurInexistantException, MdpIncorrecteException {
        if (!utilisateurs.containsKey(pseudo))
            throw new UtilisateurInexistantException();

        Utilisateur u = utilisateurs.get(pseudo);

        if (!u.getMdp().equals(mdp))
            throw new MdpIncorrecteException();

        String idConnection = UUID.randomUUID().toString();
        this.utilisateursConnectes.put(idConnection, u);
        return idConnection;
    }

    @Override
    public String checkToken(String token) throws MauvaisTokenException {
        if (!utilisateursConnectes.containsKey(token))
            throw new MauvaisTokenException();

        return utilisateursConnectes.get(token).getPseudo();
    }
}
