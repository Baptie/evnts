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

    private Map<String, Utilisateur> utilisateursConnectes;

    public FacadeAuthentificationImpl(PasswordEncoder passwordEncoder) {
        this.utilisateurs = new HashMap<>();
        this.utilisateursConnectes   = new HashMap<>();
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

    public Map<String, Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public Map<String, Utilisateur> getUtilisateursConnectes() {
        return utilisateursConnectes;
    }
}
