package gestSal.controleur;

import gestSal.facade.FacadeSalon;
import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salon")
public class ControleurSalon {

    @Autowired
    FacadeSalon facadeSalon;

    @PostMapping(value = "/creerSalon")
    public ResponseEntity<Salon> creerSalon(@RequestBody String nomCreateur, @RequestBody String nomSalon) {
        try {
            Salon salon = facadeSalon.creerSalon(nomCreateur, nomSalon);

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/salon/{nomDuSalon}")
                    .buildAndExpand(salon.getNomSalon())
                    .toUri();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, location.toString())
                    .body(salon);
        } catch (NomSalonVideException | NomTropCourtException | NumSalonDejaExistantException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/modifierSalon/{id}")
    public ResponseEntity<Salon> modifierSalon(@PathVariable int id, @RequestBody String choix,  @RequestBody String valeur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(id);

            if (salon == null) {
                throw new SalonInexistantException();
            }
            salon = facadeSalon.modifierSalon(salon, choix, valeur);

            return ResponseEntity.ok(salon);
        } catch (SalonInexistantException | NomSalonVideException | NumeroSalonVideException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getSalon/{numSalon}")
    public ResponseEntity<Salon> getSalonByNum(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(salon);
        } catch (NumeroSalonVideException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/getUtilisateur/{pseudoUtilisateur}")
    public ResponseEntity<Utilisateur> getUtilisateurByPseudo(@PathVariable String pseudoUtilisateur) {
        try {
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(utilisateur);
        } catch (NomUtilisateurVideException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/rejoindreSalon")
    public ResponseEntity<String> rejoindreSalon(@RequestBody String nomUtilisateur, @RequestBody int numSalon) {
        try {
            Salon salonRejoint = facadeSalon.getSalonByNum(numSalon);
            if (salonRejoint == null) {
                return ResponseEntity.notFound().build();
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.notFound().build();
            }
            facadeSalon.rejoindreSalon(utilisateur, salonRejoint);

            return ResponseEntity.ok("Utilisateur rejoint le salon avec succès");
        } catch (SalonInexistantException | NomSalonVideException | UtilisateurInexistantException | NomUtilisateurVideException | UtilisateurDejaPresentException e) {
            throw new RuntimeException(e);
        } catch (NumeroSalonVideException e) {
            throw new RuntimeException(e);
        }
    }
    @PatchMapping("/modifierEvenement/{id}")
    public ResponseEntity<Evenement> modifierEvenement(@PathVariable int id, @RequestBody String nomSalon, @RequestBody String choix, @RequestBody String valeur) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(id,nomSalon);

            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }

            evenement = facadeSalon.modifierEvenement(evenement, choix, valeur);

            return ResponseEntity.ok(evenement);
        } catch (EvenementInexistantException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/retirerModerateur/{numSalon}/{nomutilisateur}")
    public ResponseEntity<String> retirerModerateurDuSalon(@PathVariable int numSalon, @PathVariable String nomutilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.notFound().build();
            }

            Utilisateur utilisateurPlusModo = facadeSalon.getUtilisateurByPseudo(nomutilisateur);

            if (utilisateurPlusModo == null) {
                return ResponseEntity.notFound().build();
            }

            facadeSalon.retirerModerateurDuSalon(salon, utilisateurPlusModo);

            return ResponseEntity.ok("Modérateur retiré du salon avec succès");
        } catch (NomSalonVideException | NomUtilisateurVideException | UtilisateurPasModoException |
                 NumeroSalonVideException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/ajouterModerateurAuSalon")
    public ResponseEntity<String> ajouterModerateurAuSalon(@RequestBody String nomModo, @RequestBody int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.notFound().build();
            }
            Utilisateur nouveauModo = facadeSalon.getUtilisateurByPseudo(nomModo);

            if (nouveauModo == null) {
                return ResponseEntity.notFound().build();
            }
            facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon);

            return ResponseEntity.ok("Modérateur ajouté au salon avec succès");
        } catch (NomUtilisateurVideException | NomSalonVideException | PasAdminException |
                 UtilisateurDejaModoException | NumeroSalonVideException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/definirPresentAEvenement")
    public ResponseEntity<List<Utilisateur>> seDefiniCommePresentAUnEvenement(@RequestBody int numSalon, @RequestBody String nomEvenement, @RequestBody String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.notFound().build();
            }
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon,nomEvenement);

            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity.notFound().build();
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);

            return ResponseEntity.ok(participants);
        } catch (UtilisateurInexistantException | SalonInexistantException | EvenementInexistantException |
                 NumeroSalonVideException | NomUtilisateurVideException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/definirAbsentAEvenement/{numSalon}/{nomEvent}/{nomUtilisateur}")
    public ResponseEntity<List<Utilisateur>> seDefiniCommeAbsentAUnEvenement(
            @PathVariable int numSalon,
            @PathVariable String nomEvent,
            @PathVariable String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.notFound().build();
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon,nomEvent);

            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity.notFound().build();
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);

            return ResponseEntity.ok(participants);
        } catch (UtilisateurInexistantException | SalonInexistantException | EvenementInexistantException |
                 NumeroSalonVideException | NomUtilisateurVideException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getEvenementByNomEtNumSalon/{numSalon}")
    public ResponseEntity<Evenement> getEvenementByNomEtNumSalon(
            @PathVariable int numSalon,
            @RequestParam String nomEvenement) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);

            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(evenement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/creerEvenement")
    public ResponseEntity<Evenement> creerEvenement(@RequestBody String nomEvenement) {
        try {
            Evenement evenement = facadeSalon.creerEvenement(nomEvenement);
            return ResponseEntity.ok(evenement);
        } catch (NomEvenementDejaPrisException | NomEvenementVideException e) {
            throw new RuntimeException(e);
        }
    }


    @PatchMapping("/validerEvenement/{idSalon}/{nomEvent}")
    public ResponseEntity<Boolean> validerEvenement(@PathVariable int idSalon, @PathVariable String nomEvent ) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(idSalon,nomEvent);

            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }
            boolean isValide = facadeSalon.validerEvenement(evenement);

            return ResponseEntity.ok(isValide);
        } catch (EvenementInexistantException e) {
            throw new RuntimeException(e);
        }
    }
}
