package gestSal.facade;

import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;
import jdk.jshell.EvalException;

import java.util.List;

public interface FacadeSalon {


    /**
     * Permet de créer un salon
     * @param nomCreateur Nom de l'adiminstrateur/créateur du salon
     * @param nomSalon Nom du sallon
     * @return Le salon créé
     * @throws NomSalonVideException
     * @throws NomTropCourtException
     * @throws NumSalonDejaExistantException
     */
    Salon creerSalon(String nomCreateur, String nomSalon) throws NomSalonVideException, NomTropCourtException, NumSalonDejaExistantException;

    /**
     * Permet de modifier un salon
     * @param choix Choix du champ à modifier
     * @param numeroSalon Numéro du salon
     * @return Le salon modifié
     * @throws SalonInexistantException
     * @throws NomSalonVideException
     * @throws NumeroSalonVideException
     */
    Salon modifierSalon(int numeroSalon, String choix) throws SalonInexistantException, NomSalonVideException, NumeroSalonVideException;

    /**
     * Permet de rejoindre un salon après acceptation de celle ci
     * @param utilisateur Utilisateur ajouté
     * @param salonRejoint Salon que l'utilisateur rejoint
     * @throws SalonInexistantException
     * @throws NomSalonVideException
     * @throws UtilisateurInexistantException
     * @throws NomUtilisateurVideException
     */
    void rejoindreSalon(Utilisateur utilisateur, Salon salonRejoint) throws SalonInexistantException, NomSalonVideException, UtilisateurInexistantException,NomUtilisateurVideException;

    /**
     * Génère un lien d'invitation pour rejoidnre un salon
     * @param salonInvite Salon où l'utilisateur est invité
     * @param utilisateurInvite Utilisateur invité
     * @return Lien d'invitation
     * @throws SalonInexistantException
     * @throws UtilisateurInexistantException
     * @throws NomSalonVideException
     * @throws NomUtilisateurVideException
     */
    String inviterUtilisateur(Salon salonInvite, Utilisateur utilisateurInvite) throws SalonInexistantException, UtilisateurInexistantException, NomSalonVideException, NomUtilisateurVideException;

    /**
     * Permet de récupérer un utilisateur par son Nom
     * @param pseudoUtilisateur Pseudo de l'utilisateur
     * @return L'utilisateur avec le nom rentré
     * @throws NomUtilisateurVideException
     */
    Utilisateur getUtilisateurByPseudo(String pseudoUtilisateur) throws NomUtilisateurVideException;

    /**
     * Permet de récupérer le Salon à partir de son numéro
     * @param numSalon Numéro du salon à récupérer
     * @return Le salon correspondant au numéro inscrit
     * @throws NumeroSalonVideException
     */
    Salon getSalonByNum(int numSalon) throws NumeroSalonVideException;


    /**
     * Permet de retirer un modérateur du salon
     * @param nomSalon Le salon où le modérateur sera retiré
     * @param utilisateurPlusModo L'utilisateur qui ne sera plus modo
     * @throws NomSalonVideException
     * @throws NomUtilisateurVideException
     */
    void retirerModerateurDuSalon(Salon nomSalon, Utilisateur utilisateurPlusModo) throws NomSalonVideException, NomUtilisateurVideException;


    /**
     * Permet d'ajouter un modo à un salon
     * @param nouveauModo Utilisateur qui sera modo sur le salon
     * @param salonPourLeNouveauModo Salon où l'utilisateur sera modo
     * @throws NomUtilisateurVideException
     * @throws NomSalonVideException
     * @throws PasAdminException
     */
    void ajouterModerateurAuSalon(Utilisateur nouveauModo, Salon salonPourLeNouveauModo) throws NomUtilisateurVideException, NomSalonVideException, PasAdminException;


    /**
     * Permet de confirmer sa présence à un évenement
     * @param utilisateur utilisateur confirmant sa présence
     * @param salon Salon où l'évènement se situe
     * @param evenement Evenement concerné
     * @return La liste des utilisateurs ayant validés leur présence
     * @throws UtilisateurInexistantException
     * @throws SalonInexistantException
     * @throws EvenementInexistantException
     */
    List<Utilisateur> seDefiniCommePresentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws UtilisateurInexistantException, SalonInexistantException, EvenementInexistantException;

    /**
     * Permet de retirer la présence à un évenement
     * @param utilisateur utilisateur retirant sa présence
     * @param salon Salon où l'évènement se situe
     * @param evenement Evenement concerné
     * @return La liste des utilisateurs ayant validés leur présence
     * @throws UtilisateurInexistantException
     * @throws SalonInexistantException
     * @throws EvenementInexistantException
     */
    List<Utilisateur> seDefiniCommeAbsentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws UtilisateurInexistantException, SalonInexistantException, EvenementInexistantException;


    /**
     * Permet de récupérer un évenement grace au num de salon et au nom de l'évènement
     * @param numSalon Numéro du salon
     * @param nomEvenement Nom de l'évènement
     * @return L'évènement demandé
     */
    Evenement getEvenementByNomEtNumSalon(int numSalon, String nomEvenement);


    /**
     * Permet de créer un évenement
     * @param nomEvenement Nom de l'évènement
     * @return L'évènement créer
     * @throws NomEvenementDejaPrisException
     * @throws NomEvenementVideException
     */
    Evenement creerEvenement(String nomEvenement) throws NomEvenementDejaPrisException, NomEvenementVideException;


    /**
     * Permet de modifier un évènement
     * @param choix du champ à modifier
     * @return L'évènement modifié
     * @throws EvenementInexistantException
     */
    Evenement modifierEvenement(String choix) throws EvenementInexistantException;


    /**
     * Permet de valider ou non un évènement
     * @param evenement Eveenement a valider
     * @return True si l'évènement est validé, False sinon
     * @throws EvenementInexistantException
     */
    boolean validerEvenement(Evenement evenement) throws EvenementInexistantException;

}
