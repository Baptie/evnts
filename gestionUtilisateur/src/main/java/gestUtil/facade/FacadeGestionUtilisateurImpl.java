package gestUtil.facade;

import gestUtil.dto.UtilisateurPublicDTO;
import gestUtil.exceptions.EMailDejaPrisException;
import gestUtil.exceptions.MdpIncorrectException;
import gestUtil.exceptions.PseudoDejaPrisException;
import gestUtil.exceptions.UtilisateurNonTrouveException;
import gestUtil.modele.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component("facadeUtilisateur")
public class FacadeGestionUtilisateurImpl implements IFacadeGestionUtilisateur {

    private Map<String, Utilisateur> utilisateurs;

    private Map<String, Utilisateur> utilisateursConnectes;

    public FacadeGestionUtilisateurImpl() {
        this.utilisateurs = new HashMap<>();
        this.utilisateursConnectes = new HashMap<>();

    }
    @Override
    public void creerCompte(String pseudo, String email, String bio, String photoDeProfil) throws PseudoDejaPrisException, EMailDejaPrisException {
        if (utilisateurs.containsKey(email))
            throw new EMailDejaPrisException();

        for (Utilisateur utilisateur : utilisateurs.values())
            if (utilisateur.getPseudo().equals(pseudo))
                throw new PseudoDejaPrisException();

        this.utilisateurs.put(email,new Utilisateur(email,pseudo,bio,photoDeProfil,new ArrayList<>()));
    }

    @Override
    public void changerPseudo(String email, String pseudo) throws UtilisateurNonTrouveException, PseudoDejaPrisException {
        Utilisateur utilisateur = utilisateurs.get(email);

        if (utilisateur == null) {
            throw new UtilisateurNonTrouveException();
        }

        for(Utilisateur u : utilisateurs.values()){
            if(u.getPseudo().equals(pseudo)){
                throw new PseudoDejaPrisException();
            }
        }

        utilisateur.setPseudo(pseudo);
    }

    /*
    @Override
    public void changerMdp(String email, String ancienMdp, String nouveauMdp) throws UtilisateurNonTrouveException, MdpIncorrectException {
        Utilisateur utilisateur = utilisateurs.get(email);

        if (utilisateur == null) {
            throw new UtilisateurNonTrouveException();
        }

        if (!utilisateur.getMdp().equals(ancienMdp)) {
            throw new MdpIncorrectException();
        }

        utilisateur.setMdp(nouveauMdp);
    }*/

    @Override
    public void changerBio(String email, String nouvelleBio) throws UtilisateurNonTrouveException {
        Utilisateur utilisateur = utilisateurs.get(email);

        if (utilisateur == null) {
            throw new UtilisateurNonTrouveException();
        }

        utilisateur.setBio(nouvelleBio);
    }

    @Override
    public void changerPhotoDeProfil(String email, String nouvellePhotoDeProfil) throws UtilisateurNonTrouveException {
        Utilisateur utilisateur = utilisateurs.get(email);

        if (utilisateur == null) {
            throw new UtilisateurNonTrouveException();
        }

        utilisateur.setPhotoDeProfil(nouvellePhotoDeProfil);
    }

    @Override
    public void supprimerCompte(String email) throws UtilisateurNonTrouveException {
        if (!utilisateurs.containsKey(email)) {
            throw new UtilisateurNonTrouveException();
        }

        utilisateurs.remove(email);
    }

    @Override
    public UtilisateurPublicDTO getInformationsPubliques(String pseudo) throws UtilisateurNonTrouveException {

        for (Utilisateur utilisateur : utilisateurs.values()){
            if(utilisateur.getPseudo().equals(pseudo)){
                return new UtilisateurPublicDTO(utilisateurs.get(utilisateur.getEmail()).getPseudo(),
                        utilisateurs.get(utilisateur.getEmail()).getBio(),
                        utilisateurs.get(utilisateur.getEmail()).getPhotoDeProfil(),
                        utilisateurs.get(utilisateur.getEmail()).getListeContacts());
            }
        }
        throw new UtilisateurNonTrouveException();

    }

    @Override
    public void ajoutContact(String pseudo1, String pseudo2) throws UtilisateurNonTrouveException {
        Utilisateur utilisateur1 = null;
        Utilisateur utilisateur2 = null;

        for (Utilisateur utilisateur : utilisateurs.values()) {
            if (utilisateur.getPseudo().equals(pseudo1)) {
                utilisateur1 = utilisateur;
            } else if (utilisateur.getPseudo().equals(pseudo2)) {
                utilisateur2 = utilisateur;
            }

            if (utilisateur1 != null && utilisateur2 != null) {
                break;
            }
        }

        if (utilisateur1 == null || utilisateur2 == null) {
            throw new UtilisateurNonTrouveException();
        }

        utilisateur1.ajouterPseudoListeContact(pseudo2);
        utilisateur2.ajouterPseudoListeContact(pseudo1);


    }
}
