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



    @PatchMapping("/{numSalon}")
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



    @GetMapping("/{numSalon}")
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



    @GetMapping("/byname/{nomSalon}")
    public ResponseEntity<?> getSalonByNom(@PathVariable String nomSalon){
        try{
            //Salon salon = facadeSalon.getSalonByNom(nomSalon);
            SalonDTO salonDTO = salonSql.getSalonByName(nomSalon);
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
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(pseudoUtilisateur);
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


    @PostMapping("/rejoindreSalon")
    public ResponseEntity<ApiResponseSalonDTO> rejoindreSalon(@RequestBody String nomUtilisateur, @RequestBody int numSalon) {
        try {
            Salon salonRejoint = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if (salonRejoint == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalonDTO("Salon introuvable"));
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalonDTO("Utilisateur introuvable"));
            }

            facadeSalon.rejoindreSalon(utilisateur, salonRejoint);
            salonSql.rejoindreSalonSql(utilisateurDTO, salonDTO);

            return ResponseEntity.ok(new ApiResponseSalonDTO("Utilisateur rejoint le salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalonDTO("Erreur : " + e.getMessage()));
        }
    }
    @PatchMapping("/modifierEvenement/{id}")
    public ResponseEntity<ApiResponseEvenement> modifierEvenement(@PathVariable int id, @RequestBody String nomEvenement, @RequestBody String choix, @RequestBody String valeur) {
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(id, nomEvenement);
            EvenementDTO evenementDTO = salonSql.getEvenementByNomEtNumSalonSQL(id,nomEvenement);

            if (evenementDTO == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseEvenement("Événement introuvable"));
            }

            evenement = facadeSalon.modifierEvenement(evenement, choix, valeur);
            evenementDTO = salonSql.modifierEvenementSQL(evenementDTO, choix, valeur);

            return ResponseEntity.ok(new ApiResponseEvenement(evenementDTO));
        } catch (EvenementInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseEvenement("Erreur lors de la modification de l'événement : " + e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/retirerModerateur/{numSalon}/{nomutilisateur}")
    public ResponseEntity<ApiResponseSalon> retirerModerateurDuSalon(@PathVariable int numSalon, @PathVariable String nomutilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur utilisateurPlusModo = facadeSalon.getUtilisateurByPseudo(nomutilisateur);
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomutilisateur);
            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.retirerModerateurDuSalon(salon, utilisateurPlusModo);
            salonSql.retirerModerateurDuSalonSQL(salonDTO,utilisateurDTO);

            return ResponseEntity.ok(new ApiResponseSalon("Modérateur retiré du salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }

    @PostMapping("/ajouterModerateurAuSalon")
    public ResponseEntity<ApiResponseSalon> ajouterModerateurAuSalon(@RequestBody String nomModo, @RequestBody int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Salon introuvable"));
            }

            Utilisateur nouveauModo = facadeSalon.getUtilisateurByPseudo(nomModo);
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomModo);
            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseSalon("Utilisateur introuvable"));
            }

            facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon);
            salonSql.ajouterModerateurAuSalon(utilisateurDTO,salonDTO);
            return ResponseEntity.ok(new ApiResponseSalon("Modérateur ajouté au salon avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseSalon("Erreur : " + e.getMessage()));
        }
    }



    @PostMapping("{numSalon}/evenement/{nomEvenement}/presence")
    public ResponseEntity<Object> seDefiniCommePresentAUnEvenement(@RequestBody int numSalon, @RequestBody String nomEvenement, @RequestBody String nomUtilisateur) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }

            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);
            EvenementDTO evenementDTO = salonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvenement);

            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomUtilisateur);
            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }

            List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);
            List<String> participantsSQL = salonSql.seDefiniCommePresentAUnEvenementSQL(utilisateurDTO,evenementDTO);

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
            EvenementDTO evenementDTO = salonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvenement);
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
            EvenementDTO evenementDTO = salonSql.getEvenementByNomEtNumSalonSQL(idSalon,nomEvent);
            if (evenementDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            boolean isValide = facadeSalon.validerEvenement(evenement);
            if(isValide){
                salonSql.validerEvenementSQL(evenementDTO);
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
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            facadeSalon.envoyerMessageSalon(salon, messageDTO.getAuteur(), messageDTO.getContenu());
            salonSql.envoyerMessageSalonSQL(salonDTO,messageDTO);
            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("{numSalon}/evenement/{nomEvent}/messages")
    public ResponseEntity<Object> envoyerMessageEvenement(@PathVariable int numSalon, @PathVariable String nomEvent,@RequestBody MessageDTO messageDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement = null;
            EvenementDTO evenementDTO = salonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvent);
            for(Evenement event : salon.getLesEvenements()){
                if(event.getNomEvenement().equals(nomEvent) ){
                    evenement = event;
                }
            }
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }
            facadeSalon.envoyerMessageEvenement(evenement,messageDTO.getAuteur(), messageDTO.getContenu());
            salonSql.envoyerMessageEventSQL(evenementDTO,messageDTO);

            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("{numSalon}/messages")
    public ResponseEntity<Object> getMessagesSalon(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);
            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            List<Message> messages = facadeSalon.getMessagesSalon(salon);
            List<MessageDTO> messageDTOS = salonSql.getMessageSalonSQL(numSalon);
            return ResponseEntity.ok(messageDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("{numSalon}/evenement/{nomEvent}/messages")
    public ResponseEntity<Object> getMessagesEvenement(@PathVariable int numSalon,@PathVariable String nomEvent) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement = null;
            EvenementDTO evenementDTO = salonSql.getEvenementByNomEtNumSalonSQL(numSalon,nomEvent);
            for(Evenement event : salon.getLesEvenements()){
                if(event.getNomEvenement()==nomEvent){
                    evenement = event;
                }
            }
            if (evenementDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            List<Message> messages = facadeSalon.getMessagesEvenement(evenement);
            List<MessageDTO> messageDTOS = salonSql.getMessageEventSQL(evenementDTO.getIdEvenement());

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
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if (salonDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(pseudoUtilisateur);

            if (utilisateurDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }
            facadeSalon.rejoindreSalon(utilisateur,salon);
            salonSql.rejoindreSalonSql(utilisateurDTO,salonDTO);

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
            salonSql.supprimerUtilisateurSQL(facadeSalon.getUtilisateurByPseudo(nomUtilisateur));
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
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomUtilisateur);
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
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomModerateur);
            if(utilisateurDTO==null){
                throw new UtilisateurInexistantException();
            }

            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if(salonDTO==null){
                throw new SalonInexistantException();
            }
            facadeSalon.ajouterModerateurAuSalon(utilisateur,salon);
            salonSql.ajouterModerateurAuSalon(utilisateurDTO,salonDTO);

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
            UtilisateurDTO utilisateurDTO = salonSql.getUtilisateurByPseudoSQL(nomModerateur);

            if(utilisateur==null){
                throw new UtilisateurInexistantException();
            }
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            SalonDTO salonDTO = salonSql.getSalonById(numSalon);

            if(salonDTO==null){
                throw new SalonInexistantException();
            }
            facadeSalon.retirerModerateurDuSalon(salon,utilisateur);
            salonSql.retirerModerateurDuSalonSQL(salonDTO,utilisateurDTO);

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
