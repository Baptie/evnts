package gestSal.controleur;

import gestSal.apireponses.ApiResponseEvenement;
import gestSal.apireponses.ApiResponseSalon;
import gestSal.apireponses.ApiResponseSalonDTO;
import gestSal.apireponses.ApiResponseUtilisateur;
import gestSal.dto.EvenementDTO;
import gestSal.dto.MessageDTO;
import gestSal.dto.SalonDTO;
import gestSal.dto.UtilisateurDTO;
import gestSal.facade.FacadeSalon;
import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
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


    @PostMapping(value = "")
    public ResponseEntity<ApiResponseSalon> creerSalon(@RequestBody String nomCreateur, @RequestBody String nomSalon) {
        try {
            Salon salon = facadeSalon.creerSalon(nomCreateur, nomSalon);

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
        }
    }



    @PatchMapping("/{numSalon}")
    public ResponseEntity<ApiResponseSalon> modifierSalon(@PathVariable int numSalon, @RequestBody String choix, @RequestBody String valeur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalon("Salon introuvable"));
            }

            salon = facadeSalon.modifierSalon(salon, choix, valeur);
            return ResponseEntity.ok(new ApiResponseSalon(salon));
        } catch (SalonInexistantException | NomSalonVideException | NumeroSalonVideException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalon("Erreur lors de la modification du salon : " + e.getMessage()));
        }
    }



    @GetMapping("/{numSalon}")
    public ResponseEntity<ApiResponseSalonDTO> getSalonByNum(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);
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



    @GetMapping("/byname/{nomSalon}")
    public ResponseEntity<?> getSalonByNom(@PathVariable String nomSalon){
        try{
            Salon salon = facadeSalon.getSalonByNom(nomSalon);
            SalonDTO salonDTO = SalonSql.getSalonByName(nomSalon);
            if(salonDTO==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            return ResponseEntity.ok(salonDTO);

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }
    @GetMapping("/getUtilisateur/{pseudoUtilisateur}")
    public ResponseEntity<ApiResponseUtilisateur> getUtilisateurByPseudo(@PathVariable String pseudoUtilisateur) {
        try {
//            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(pseudoUtilisateur);
            Utilisateur utilisateur = facadeSalon.convertUserDTOtoUser(utilisateurDTO);
            if (utilisateur == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseUtilisateur("Utilisateur introuvable"));
            }

            return ResponseEntity.ok(new ApiResponseUtilisateur(utilisateur));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/salon/adhesion")
    public ResponseEntity<ApiResponseSalonDTO> rejoindreSalon(@RequestBody String nomUtilisateur, @RequestBody int numSalon) {
        try {
            Salon salonRejoint = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if (salonRejoint == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalonDTO("Salon introuvable"));
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalonDTO("Utilisateur introuvable"));
            }

            facadeSalon.rejoindreSalon(utilisateur, salonRejoint);
            SalonSql.rejoindreSalonSql(utilisateurDTO, salonDTO);

            return ResponseEntity.ok(new ApiResponseSalonDTO("Utilisateur rejoint le salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalonDTO("Erreur : " + e.getMessage()));
        }
    }
    @PatchMapping("{numSalon}/evenement/{id}")
    public ResponseEntity<ApiResponseEvenement> modifierEvenement(@PathVariable int id, @PathVariable String nomEvenement, @RequestBody String choix, @RequestBody String valeur) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(id, nomEvenement);
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(id,nomEvenement);

            if (evenementDTO == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseEvenement("Événement introuvable"));
            }

            evenement = facadeSalon.modifierEvenement(evenement, choix, valeur);
            evenementDTO = SalonSql.modifierEvenementSQL(evenementDTO, choix, valeur);

            return ResponseEntity.ok(new ApiResponseEvenement(evenementDTO));
        } catch (EvenementInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseEvenement("Erreur lors de la modification de l'événement : " + e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/{numSalon}/{nomutilisateur}")
    public ResponseEntity<ApiResponseSalon> retirerModerateurDuSalon(@PathVariable int numSalon, @PathVariable String nomutilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur utilisateurPlusModo = facadeSalon.getUtilisateurByPseudo(nomutilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomutilisateur);
            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.retirerModerateurDuSalon(salon, utilisateurPlusModo);
            SalonSql.retirerModerateurDuSalonSQL(salonDTO,utilisateurDTO);

            return ResponseEntity.ok(new ApiResponseSalon("Modérateur retiré du salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }

    @PostMapping("/ajouterModerateurAuSalon")
    public ResponseEntity<ApiResponseSalon> ajouterModerateurAuSalon(@RequestBody String nomModo, @RequestBody int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur nouveauModo = facadeSalon.getUtilisateurByPseudo(nomModo);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomModo);
            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon);
            SalonSql.ajouterModerateurAuSalon(utilisateurDTO,salonDTO);
            return ResponseEntity.ok(new ApiResponseSalon("Modérateur ajouté au salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }



    @PostMapping("{numSalon}/evenement/{nomEvenement}/presence")
    public ResponseEntity<Object> seDefiniCommePresentAUnEvenement(@RequestBody int numSalon, @RequestBody String nomEvenement, @RequestBody String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvenement);

            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomUtilisateur);
            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);
            List<String> participantsSQL = SalonSql.seDefiniCommePresentAUnEvenementSQL(utilisateurDTO,evenementDTO);

            return ResponseEntity.ok(participantsSQL);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @DeleteMapping("{numSalon}/evenement/{nomEvent}/presence")
    public ResponseEntity<Object> seDefiniCommeAbsentAUnEvenement(@PathVariable int numSalon, @PathVariable String nomEvent, @PathVariable String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvent);
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvent);
            if (evenementDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomUtilisateur);

            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);
            List<String> participantsDTO = SalonSql.seDefiniCommeAbsentAUnEvenementSQL(utilisateurDTO, evenementDTO);
            return ResponseEntity.ok(participantsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }
    @PostMapping("{numSalon}/evenement")
    public ResponseEntity<Object> creerEvenement(@PathVariable int numSalon, @RequestBody EvenementDTO evenementDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            Utilisateur createur = facadeSalon.getUtilisateurByPseudo(evenementDTO.getNomCreateur());
            Evenement evenement = facadeSalon.creerEvenement(salon, evenementDTO.getNomEvenement(), evenementDTO.getNombrePersonneMax(), evenementDTO.getDetailsEvenement(), evenementDTO.getLieu(), createur, evenementDTO.getDate());
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(evenement.getIdEvenement()  )
                    .toUri();
            return ResponseEntity.created(location).body(evenement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("{numSalon}/evenement/{nomEvenement}")
    public ResponseEntity<Object> getEvenementByNomEtNumSalon(@PathVariable int numSalon, @RequestParam String nomEvenement) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvenement);
            if (evenementDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            return ResponseEntity.ok(evenementDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }




    @PatchMapping("{numSalon}/evenement/{nomEvent}/validation")
    public ResponseEntity<Object> validerEvenement(@PathVariable int idSalon, @PathVariable String nomEvent) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(idSalon, nomEvent);
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(idSalon,nomEvent);
            if (evenementDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            boolean isValide = facadeSalon.validerEvenement(evenement);
            if(isValide){
                SalonSql.validerEvenementSQL(evenementDTO);
            }

            return ResponseEntity.ok(isValide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @PostMapping("{numSalon}/messages")
    public ResponseEntity<Object> envoyerMessageSalon(@PathVariable int numSalon, @RequestBody MessageDTO messageDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            facadeSalon.envoyerMessageSalon(salon, messageDTO.getAuteur(), messageDTO.getContenu());
            SalonSql.envoyerMessageSalonSQL(salonDTO,messageDTO);
            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("{numSalon}/evenement/{nomEvent}/messages")
    public ResponseEntity<Object> envoyerMessageEvenement(@PathVariable int numSalon, @PathVariable String nomEvent,@RequestBody MessageDTO messageDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement = null;
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvent);
            for(Evenement event : salon.getLesEvenements()){
                if(event.getNomEvenement().equals(nomEvent) ){
                    evenement = event;
                }
            }
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }
            facadeSalon.envoyerMessageEvenement(evenement,messageDTO.getAuteur(), messageDTO.getContenu());
            SalonSql.envoyerMessageEventSQL(evenementDTO,messageDTO);

            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("{numSalon}/messages")
    public ResponseEntity<Object> getMessagesSalon(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            List<Message> messages = facadeSalon.getMessagesSalon(salon);
            List<MessageDTO> messageDTOS = SalonSql.getMessageSalonSQL(numSalon);
            return ResponseEntity.ok(messageDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("{numSalon}/evenement/{nomEvent}/messages")
    public ResponseEntity<Object> getMessagesEvenement(@PathVariable int numSalon,@PathVariable String nomEvent) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement = null;
            EvenementDTO evenementDTO = SalonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvent);
            for(Evenement event : salon.getLesEvenements()){
                if(event.getNomEvenement()==nomEvent){
                    evenement = event;
                }
            }
            if (evenementDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            List<Message> messages = facadeSalon.getMessagesEvenement(evenement);
            List<MessageDTO> messageDTOS = SalonSql.getMessageEventSQL(evenementDTO.getIdEvenement());

            return ResponseEntity.ok(messageDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PostMapping("{numSalon}/invitation")
    public ResponseEntity<Object> inviterUtilisateur(@RequestBody int numSalon, @RequestBody String pseudoUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }
            String invitationUrl = facadeSalon.inviterUtilisateur(salon, utilisateur);
            return ResponseEntity.ok("Invitation envoyée. URL: " + invitationUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PostMapping("{numSalon}/adhesion")
    public ResponseEntity<?> rejoindreSalon(@PathVariable int numSalon, @RequestBody String pseudoUtilisateur){
        try{
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(pseudoUtilisateur);

            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }
            facadeSalon.rejoindreSalon(utilisateur,salon);
            SalonSql.rejoindreSalonSql(utilisateurDTO,salonDTO);

            return ResponseEntity.ok(salonDTO);
        }catch(UtilisateurDejaPresentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Utilisateur deja présent dans salon");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @DeleteMapping("/utilisateur/{nomUtilisateur}")
    public ResponseEntity<?> supprimerUtilisateur(@PathVariable String nomUtilisateur){
        try {
            facadeSalon.supprimerUtilisateur(nomUtilisateur);
            Utilisateur user = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
            utilisateurDTO.setIdUtilisateur(user.getIdUtilisateur());
            utilisateurDTO.setPseudo(user.getPseudo());
            SalonSql.supprimerUtilisateurSQL(utilisateurDTO);

            return ResponseEntity.noContent().build();
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (SQLException | NomUtilisateurVideException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/utilisateur/{nomUtilisateur}")
    public ResponseEntity<?> getUtilisateur(@PathVariable String nomUtilisateur){
        try {
            Utilisateur user = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomUtilisateur);
            return ResponseEntity.ok(utilisateurDTO);
        } catch (NomUtilisateurVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/{numSalon}/moderation")
    public ResponseEntity<?> ajouterModerateur(@PathVariable int numSalon, @RequestBody String nomModerateur){
        try{
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomModerateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomModerateur);
            if(utilisateurDTO==null){
                throw new UtilisateurInexistantException();
            }

            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if(salonDTO==null){
                throw new SalonInexistantException();
            }
            facadeSalon.ajouterModerateurAuSalon(utilisateur,salon);
            SalonSql.ajouterModerateurAuSalon(utilisateurDTO,salonDTO);

            return ResponseEntity.ok(salon);
        } catch (NomUtilisateurVideException | NumeroSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : " + e.getMessage());
        }catch (NomSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{numSalon}/moderation")
    public ResponseEntity<?> supprimerModerateur(@PathVariable int numSalon, @RequestBody String nomModerateur){
        try{
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomModerateur);
            UtilisateurDTO utilisateurDTO = SalonSql.getUtilisateurByPseudoSQL(nomModerateur);

            if(utilisateur==null){
                throw new UtilisateurInexistantException();
            }
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = SalonSql.getSalonById(numSalon);

            if(salonDTO==null){
                throw new SalonInexistantException();
            }
            facadeSalon.retirerModerateurDuSalon(salon,utilisateur);
            SalonSql.retirerModerateurDuSalonSQL(salonDTO,utilisateurDTO);

            return ResponseEntity.ok(salonDTO);
        } catch (NomUtilisateurVideException | NumeroSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : " + e.getMessage());
        }catch (NomSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
