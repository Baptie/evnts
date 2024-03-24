package gestSal.controleur;

import gestSal.apireponses.ApiResponseEvenement;
import gestSal.apireponses.ApiResponseSalon;
import gestSal.apireponses.ApiResponseSalonDTO;
import gestSal.apireponses.ApiResponseUtilisateur;
import gestSal.dto.SalonDTO;
import gestSal.facade.FacadeSalon;
import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;
import gestSal.service.SalonSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/salon")
public class ControleurSalon {

    @Autowired
    FacadeSalon facadeSalon;


    SalonSql salonSql;

    @PostMapping(value = "/creerSalon")
    public ResponseEntity<ApiResponseSalon> creerSalon(@RequestBody String nomCreateur, @RequestBody String nomSalon) {
        try {
            Salon salon = facadeSalon.creerSalon(nomCreateur, nomSalon);

            salonSql.creerSalonSQL(nomSalon,nomCreateur,"https://e7.pngegg.com/pngimages/872/540/png-clipart-computer-icons-event-management-event-miscellaneous-angle-thumbnail.png");

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/salon/{nomDuSalon}")
                    .buildAndExpand(salon.getNomSalon())
                    .toUri();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, location.toString())
                    .body(new ApiResponseSalon(salon));
        } catch (NomSalonVideException | NomTropCourtException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalon("Erreur lors de la création du salon : " + e.getMessage()));
        }catch(NumSalonDejaExistantException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponseSalon("Erreur lors de la création du salon : " + e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/modifierSalon/{id}")
    public ResponseEntity<ApiResponseSalonDTO> modifierSalon(@PathVariable int id, @RequestBody String choix, @RequestBody String valeur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(id);
            SalonDTO salonDTO = salonSql.getSalonById(id);

            if (salon == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalonDTO("Salon introuvable"));
            }

            salon = facadeSalon.modifierSalon(salon, choix, valeur);
            salonDTO = salonSql.modifierSalonSQL(salonDTO,choix,valeur,id);
            return ResponseEntity.ok(new ApiResponseSalonDTO(salonDTO));
        } catch (SalonInexistantException | NomSalonVideException | NumeroSalonVideException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalonDTO("Erreur lors de la modification du salon : " + e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getSalon/{numSalon}")
    public ResponseEntity<ApiResponseSalonDTO> getSalonByNum(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);
            if (salon == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalonDTO("Salon introuvable"));
            }

            return ResponseEntity.ok(new ApiResponseSalonDTO(salonDTO));
        } catch (NumeroSalonVideException | SalonInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalonDTO("Erreur lors de la récupération du salon : " + e.getMessage()));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/getUtilisateur/{pseudoUtilisateur}")
    public ResponseEntity<ApiResponseUtilisateur> getUtilisateurByPseudo(@PathVariable String pseudoUtilisateur) {
        try {
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseUtilisateur("Utilisateur introuvable"));
            }

            return ResponseEntity.ok(new ApiResponseUtilisateur(utilisateur));
        } catch (NomUtilisateurVideException | UtilisateurInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseUtilisateur("Erreur lors de la récupération de l'utilisateur : " + e.getMessage()));
        }
    }


    @PostMapping("/rejoindreSalon")
    public ResponseEntity<ApiResponseSalon> rejoindreSalon(@RequestBody String nomUtilisateur, @RequestBody int numSalon) {
        try {
            Salon salonRejoint = facadeSalon.getSalonByNum(numSalon);
            if (salonRejoint == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.rejoindreSalon(utilisateur, salonRejoint);
            return ResponseEntity.ok(new ApiResponseSalon("Utilisateur rejoint le salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }
    @PatchMapping("/modifierEvenement/{id}")
    public ResponseEntity<ApiResponseEvenement> modifierEvenement(@PathVariable int id, @RequestBody String nomSalon, @RequestBody String choix, @RequestBody String valeur) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(id, nomSalon);

            if (evenement == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseEvenement("Événement introuvable"));
            }

            evenement = facadeSalon.modifierEvenement(evenement, choix, valeur);
            return ResponseEntity.ok(new ApiResponseEvenement(evenement));
        } catch (EvenementInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseEvenement("Erreur lors de la modification de l'événement : " + e.getMessage()));
        }
    }

    @DeleteMapping("/retirerModerateur/{numSalon}/{nomutilisateur}")
    public ResponseEntity<ApiResponseSalon> retirerModerateurDuSalon(@PathVariable int numSalon, @PathVariable String nomutilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur utilisateurPlusModo = facadeSalon.getUtilisateurByPseudo(nomutilisateur);
            if (utilisateurPlusModo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.retirerModerateurDuSalon(salon, utilisateurPlusModo);
            return ResponseEntity.ok(new ApiResponseSalon("Modérateur retiré du salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }

    @PostMapping("/ajouterModerateurAuSalon")
    public ResponseEntity<ApiResponseSalon> ajouterModerateurAuSalon(@RequestBody String nomModo, @RequestBody int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur nouveauModo = facadeSalon.getUtilisateurByPseudo(nomModo);
            if (nouveauModo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon);
            return ResponseEntity.ok(new ApiResponseSalon("Modérateur ajouté au salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }

    @PostMapping("/definirPresentAEvenement")
    public ResponseEntity<Object> seDefiniCommePresentAUnEvenement(@RequestBody int numSalon, @RequestBody String nomEvenement, @RequestBody String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @DeleteMapping("/definirAbsentAEvenement/{numSalon}/{nomEvent}/{nomUtilisateur}")
    public ResponseEntity<Object> seDefiniCommeAbsentAUnEvenement(@PathVariable int numSalon, @PathVariable String nomEvent, @PathVariable String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/getEvenementByNomEtNumSalon/{numSalon}")
    public ResponseEntity<Object> getEvenementByNomEtNumSalon(@PathVariable int numSalon, @RequestParam String nomEvenement) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            return ResponseEntity.ok(evenement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("/creerEvenement")
    public ResponseEntity<Object> creerEvenement(@RequestBody String nomEvenement) {
        try {
            Evenement evenement = facadeSalon.creerEvenement(nomEvenement);
            return ResponseEntity.ok(evenement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PatchMapping("/validerEvenement/{idSalon}/{nomEvent}")
    public ResponseEntity<Object> validerEvenement(@PathVariable int idSalon, @PathVariable String nomEvent) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(idSalon, nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            boolean isValide = facadeSalon.validerEvenement(evenement);
            return ResponseEntity.ok(isValide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }
}
