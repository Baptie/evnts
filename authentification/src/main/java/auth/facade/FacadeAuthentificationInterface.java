package auth.facade;

import auth.exception.*;
import auth.modele.Utilisateur;

import java.util.Map;

public interface FacadeAuthentificationInterface {

        /**
         * Inscription d'un nouveau utilisateur sur le site
         *
         * @param pseudo
         * @param mdp
         * @param eMail
         * @throws PseudoDejaPrisException
         * @throws EMailDejaPrisException
         */
        void inscription(String pseudo, String mdp, String eMail) throws PseudoDejaPrisException, EMailDejaPrisException;

        /**
         * Connexion de l'utilisateur sur le site
         *
         * @param pseudo
         * @param mdp
         * @return
         * @throws UtilisateurInexistantException
         * @throws MdpIncorrecteException
         */
        String connexion(String pseudo, String mdp) throws UtilisateurInexistantException, MdpIncorrecteException;

        boolean getStatus(String pseudo) throws UtilisateurInexistantException;

        Map<String, Utilisateur> getUtilisateurs();
}
