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

        /**
         * Déconnexion de l'utilisateur sur le site
         *
         * @param pseudo
         * @throws UtilisateurInexistantException
         */
        void deconnexion(String pseudo) throws UtilisateurInexistantException;

        boolean getStatus(String pseudo) throws UtilisateurInexistantException;

        Map<String, Utilisateur> getUtilisateurs();

        void reSetPseudo(String ancienPseudo, String nouveauPseudo) throws UtilisateurInexistantException;

        void reSetMDP(String pseudo, String mdp, String nouveauMDP) throws UtilisateurInexistantException, MdpIncorrecteException;

        void supprimerUtilisateur(String pseudo, String mdp) throws UtilisateurInexistantException, MdpIncorrecteException;
}
