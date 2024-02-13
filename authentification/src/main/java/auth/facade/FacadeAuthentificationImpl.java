package auth.facade;

import auth.exception.*;
import auth.modele.Utilisateur;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component("facadeAuth")
public class FacadeAuthentificationImpl implements FacadeAuthentificationInterface {

    private PasswordEncoder passwordEncoder;

    private Map<String, Utilisateur> utilisateurs;

    public FacadeAuthentificationImpl(PasswordEncoder passwordEncoder) {
        this.utilisateurs = new HashMap<>();
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public void inscription(String pseudo, String mdp, String eMail) throws PseudoDejaPrisException, EMailDejaPrisException {

        if (utilisateurs.containsKey(pseudo))
            throw new PseudoDejaPrisException();

        for (Utilisateur utilisateur : utilisateurs.values())
            if (utilisateur.getEMail().equals(eMail))
                throw new EMailDejaPrisException();

        this.utilisateurs.put(pseudo,new Utilisateur(pseudo,eMail,passwordEncoder.encode(mdp)));

    }

    @Override
    public String connexion(String pseudo, String mdp) throws UtilisateurInexistantException, MdpIncorrecteException {
        if (!utilisateurs.containsKey(pseudo))
            throw new UtilisateurInexistantException();

        Utilisateur u = utilisateurs.get(pseudo);

        if (!passwordEncoder.matches(mdp, u.getMdp()))
            throw new MdpIncorrecteException();

        u.setStatus(true);
        String idConnection = UUID.randomUUID().toString();
        return idConnection;
    }

    public boolean getStatus(String pseudo) throws UtilisateurInexistantException {
        if (!utilisateurs.containsKey(pseudo))
            throw new UtilisateurInexistantException();

        Utilisateur u = utilisateurs.get(pseudo);

        return u.isStatus();

    }

    public Map<String, Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

}
