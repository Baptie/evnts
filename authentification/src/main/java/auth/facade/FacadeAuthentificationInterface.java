package auth.facade;

import auth.exception.*;

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
         * @throws mdpIncorrecteException
         */
        String connexion(String pseudo, String mdp) throws UtilisateurInexistantException, mdpIncorrecteException;

        /**
         * Permet de récupérer le pseudo de l'utilisateur possédant ce token
         *
         * @param token
         * @return le pseudo correspondant au token
         * @throws MauvaisTokenException
         */
        String checkToken(String token) throws MauvaisTokenException;

}
