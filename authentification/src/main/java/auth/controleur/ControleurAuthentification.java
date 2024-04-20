package auth.controleur;

import auth.exception.*;
import auth.facade.FacadeAuthentificationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class ControleurAuthentification {

    private static final String MAUVAIS_PSEUDO = "Mauvais pseudo !";
    private static final String MAUVAIS_MDP = "Mauvais mdp !";

    @Autowired
    FacadeAuthentificationInterface facadeAuth;

    @PostMapping(value = "/inscription")
    public ResponseEntity<String> inscription(@RequestParam String pseudo, @RequestParam String mdp, @RequestParam String eMail) {
        try {
            this.facadeAuth.inscription(pseudo,mdp,eMail);
            return ResponseEntity.created(null).body("Compte créé !");
        } catch (PseudoDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pseudo "+pseudo+" déjà pris");
        }catch (EMailDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email "+eMail+" déjà existante");
        } catch (EmailOuPseudoDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email ou pseudo déjà existante");
        }
    }


    @PostMapping(value = "/connexion")
    public ResponseEntity<String> connexion (@RequestParam String pseudo, @RequestParam String mdp){
        try {
            String token = this.facadeAuth.connexion(pseudo,mdp);
            return ResponseEntity.status(HttpStatus.OK).header("token",token).body(token);
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mauvais identifiants !");
        } catch (MdpIncorrecteException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mauvais mot de passe");
        }
    }

    @PostMapping(value = "/deconnexion")
    public ResponseEntity<String> deconnexion (@RequestParam String pseudo){
        try {
            this.facadeAuth.deconnexion(pseudo);
            return ResponseEntity.ok("Déconnexion de "+pseudo+" faite !");
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mauvais identifiants !");
        } catch (UtilisateurDejaDeconnecteException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Déjà connecté !");
        }
    }

    @PatchMapping (value = "/modification-pseudo")
    public ResponseEntity<String> modificationPseudo (@RequestParam String pseudo, @RequestParam String nouveauPseudo){
        try{
            this.facadeAuth.reSetPseudo(pseudo,nouveauPseudo);
            return ResponseEntity.ok("Pseudo : "+pseudo+" changé en :" + nouveauPseudo+" !");
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MAUVAIS_PSEUDO);
        }
    }

    @PatchMapping(value = "/modification-mdp")
    ResponseEntity<String> modificationMdp (@RequestParam String pseudo,@RequestParam String mdp, @RequestParam String nouveauMDP){
        try{
            this.facadeAuth.reSetMDP(pseudo,mdp,nouveauMDP);
            return ResponseEntity.ok("Mdp changé !");
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MAUVAIS_PSEUDO);
        } catch (MdpIncorrecteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MAUVAIS_MDP);
        }
    }

    @DeleteMapping (value = "/suppression")
    ResponseEntity<String> suppressionUtilisateur (@RequestParam String pseudo,@RequestParam String mdp){
        try{
            this.facadeAuth.supprimerUtilisateur(pseudo,mdp);
            return ResponseEntity.ok("Utilisateur supprimé");
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MAUVAIS_PSEUDO);
        } catch (MdpIncorrecteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MAUVAIS_MDP);
        }
    }

}
