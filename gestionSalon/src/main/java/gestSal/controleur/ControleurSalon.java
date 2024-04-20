package gestSal.controleur;

import gestSal.apireponses.ApiResponseEvenement;
import gestSal.apireponses.ApiResponseSalon;
import gestSal.apireponses.ApiResponseSalonDTO;
import gestSal.apireponses.ApiResponseUtilisateur;
import gestSal.dto.EvenementDTO;
import gestSal.dto.MessageDTO;
import gestSal.dto.SalonDTO;
import gestSal.facade.FacadeSalon;
import gestSal.facade.erreurs.*;
import gestSal.modele.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/salon")
public class ControleurSalon {

    final
    FacadeSalon facadeSalon;

    public ControleurSalon(FacadeSalon facadeSalon) {
        this.facadeSalon = facadeSalon;
    }


    @PostMapping(value = "")
    public ResponseEntity<ApiResponseSalon> creerSalon(@RequestParam String nomCreateur, @RequestParam String nomSalon) {
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
    public ResponseEntity<ApiResponseSalon> modifierSalon(@PathVariable int numSalon, @RequestBody SalonModificationRequest modifs) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalon("Salon introuvable"));
            }

            salon = facadeSalon.modifierSalon(salon, modifs.getChoix(), modifs.getValeur());
            return ResponseEntity.ok(new ApiResponseSalon(salon));
        } catch (SalonInexistantException | NomSalonVideException | NumeroSalonVideException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalon("Erreur lors de la modification du salon : " + e.getMessage()));
        }
    }



    @GetMapping("/{numSalon}")
    public ResponseEntity<ApiResponseSalon> getSalonByNum(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseSalon("Salon introuvable"));
            }

            return ResponseEntity.ok(new ApiResponseSalon(salon));
        } catch (NumeroSalonVideException | SalonInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseSalon("Erreur lors de la récupération du salon : " + e.getMessage()));
        }

    }



    @GetMapping("/byname/{nomSalon}")
    public ResponseEntity<?> getSalonByNom(@PathVariable String nomSalon){
        try{
            Salon salon = facadeSalon.getSalonByNom(nomSalon);
            if(salon==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            return ResponseEntity.ok(salon);

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/getUtilisateur/{pseudoUtilisateur}")
    public ResponseEntity<ApiResponseUtilisateur> getUtilisateurByPseudo(@PathVariable String pseudoUtilisateur) {
        Utilisateur utilisateur;
        try {
            utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);
        } catch (NomUtilisateurVideException | UtilisateurInexistantException e) {
            throw new RuntimeException(e);
        }
        if (utilisateur == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseUtilisateur("Utilisateur introuvable"));
        }
        return ResponseEntity.ok(new ApiResponseUtilisateur(utilisateur));
    }

    @PatchMapping("/{id}/evenement/{idEvent}")
    public ResponseEntity<ApiResponseEvenement> modifierEvenement(@PathVariable int id, @PathVariable int idEvent, @RequestBody SalonModificationRequest modifs) {
        try {
            String nomEvent = facadeSalon.getNomEvenementById(idEvent);
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(id, nomEvent);

            if (evenement == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseEvenement("Événement introuvable"));
            }

            evenement = facadeSalon.modifierEvenement(id,evenement, modifs.getChoix(),modifs.getValeur());

            return ResponseEntity.ok(new ApiResponseEvenement(evenement));
        } catch (EvenementInexistantException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseEvenement("Erreur lors de la modification de l'événement : " + e.getMessage()));
        }
    }





    @PostMapping("{numSalon}/evenement/{idEvent}/presence")
    public ResponseEntity<Object> seDefiniCommePresentAUnEvenement(@RequestParam String nomUtilisateur, @PathVariable int idEvent, @PathVariable int numSalon) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
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

            List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);

            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @DeleteMapping("{numSalon}/evenement/{idEvent}/presence")
    public ResponseEntity<Object> seDefiniCommeAbsentAUnEvenement(@PathVariable int numSalon, @PathVariable int idEvent, @RequestParam String nomUtilisateur) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
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


    @GetMapping("{numSalon}/evenement/{idEvent}")
    public ResponseEntity<Object> getEvenementByNomEtNumSalon(@PathVariable int numSalon, @PathVariable int idEvent)   {
        String nomEvenement = facadeSalon.getNomEvenementById(idEvent);
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




    @PatchMapping("{numSalon}/evenement/{idEvent}/validation")
    public ResponseEntity<Object> validerEvenement(@PathVariable int numSalon, @PathVariable int idEvent) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            boolean isValide = facadeSalon.validerEvenement(evenement);


            return ResponseEntity.ok(isValide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @PostMapping("{numSalon}/messages")
    public ResponseEntity<Object> envoyerMessageSalon(@PathVariable int numSalon, @RequestBody MessageDTO messageDTO) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            facadeSalon.envoyerMessageSalon(salon, messageDTO.getAuteur(), messageDTO.getContenu());
            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("{numSalon}/evenement/{idEvent}/messages")
    public ResponseEntity<Object> envoyerMessageEvenement(@PathVariable int numSalon, @PathVariable int idEvent,@RequestBody MessageDTO messageDTO) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon,nomEvent);
                   if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }
            facadeSalon.envoyerMessageEvenement(evenement,messageDTO.getAuteur(), messageDTO.getContenu());

            return ResponseEntity.ok("Message envoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("{numSalon}/messages")
    public ResponseEntity<Object> getMessagesSalon(@PathVariable int numSalon) {
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);
            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            List<Message> messages = facadeSalon.getMessagesSalon(salon);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("{numSalon}/evenement/{idEvent}/messages")
    public ResponseEntity<Object> getMessagesEvenement(@PathVariable int numSalon,@PathVariable int idEvent) {
        String nomEvent = facadeSalon.getNomEvenementById(idEvent);
        try {
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Evenement evenement;
            evenement = facadeSalon.getEvenementByNomEtNumSalon(numSalon,nomEvent);
            if (evenement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement introuvable");
            }

            List<Message> messages = facadeSalon.getMessagesEvenement(evenement);

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    //TODO BACK ?
    @PostMapping("{numSalon}/invitation")
    public ResponseEntity<Object> inviterUtilisateur(@RequestBody String pseudoUtilisateur, @PathVariable int numSalon) {
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
    public ResponseEntity<?> rejoindreSalon(@PathVariable int numSalon, @RequestParam String pseudoUtilisateur){
        try{
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if (salon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon introuvable");
            }
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(pseudoUtilisateur);

            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable");
            }
            facadeSalon.rejoindreSalon(utilisateur,salon);

            return ResponseEntity.ok(salon);
        }catch(UtilisateurDejaPresentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Utilisateur deja présent dans salon");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @DeleteMapping("/utilisateur/{nomUtilisateur}")
    public ResponseEntity<?> supprimerUtilisateur(@PathVariable String nomUtilisateur){
        try {
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            facadeSalon.supprimerUtilisateur(utilisateur);
            return ResponseEntity.noContent().build();
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (NomUtilisateurVideException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/utilisateur/{nomUtilisateur}")
    public ResponseEntity<?> getUtilisateur(@PathVariable String nomUtilisateur){
        try {
            Utilisateur user = facadeSalon.getUtilisateurByPseudo(nomUtilisateur);
            return ResponseEntity.ok(user);
        } catch (NomUtilisateurVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/{numSalon}/moderation")
    public ResponseEntity<?> ajouterModerateur(@PathVariable int numSalon, @RequestParam String nomModerateur){
        try{
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomModerateur);
            if(utilisateur==null){
                throw new UtilisateurInexistantException();
            }

            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if(salon==null){
                throw new SalonInexistantException();
            }
            facadeSalon.ajouterModerateurAuSalon(utilisateur,salon);

            return ResponseEntity.ok(salon);
        } catch (NomUtilisateurVideException | NumeroSalonVideException | NomSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{numSalon}/moderation")
    public ResponseEntity<?> supprimerModerateur(@PathVariable int numSalon, @RequestParam String nomModerateur){
        try{
            Utilisateur utilisateur = facadeSalon.getUtilisateurByPseudo(nomModerateur);

            if(utilisateur==null){
                throw new UtilisateurInexistantException();
            }
            Salon salon = facadeSalon.getSalonByNum(numSalon);

            if(salon==null){
                throw new SalonInexistantException();
            }
            facadeSalon.retirerModerateurDuSalon(salon,utilisateur);

            return ResponseEntity.ok(salon);
        } catch (NomUtilisateurVideException | NumeroSalonVideException | NomSalonVideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/utilisateur/{nomUtilisateur}/salons")
    public List<Salon> recupererSalonDeLutilisateur(@PathVariable String nomUtilisateur) throws AucunSalonException, NomUtilisateurVideException, UtilisateurInexistantException, SalonInexistantException, NumeroSalonVideException {
        int idUser = facadeSalon.getUtilisateurByPseudo(nomUtilisateur).getIdUtilisateur();
        List<Integer> lesSalonsDeLUser = facadeSalon.getSalonByUser(idUser);
        List<Salon> lesSalons = new ArrayList<>();
        if(lesSalonsDeLUser.isEmpty()){
            throw new AucunSalonException();
        }else{
            for(int idSalon : lesSalonsDeLUser){
                lesSalons.add(facadeSalon.getSalonByNum(idSalon));
            }
        }
        return lesSalons;
    }

    @GetMapping("/utilisateur/{nomUtilisateur}/evenements")
    public List<Evenement> recupererEventDeLutilisateur(@PathVariable String nomUtilisateur) throws NomUtilisateurVideException, UtilisateurInexistantException, SalonInexistantException, NumeroSalonVideException, AucunEventException {
        int idUser = facadeSalon.getUtilisateurByPseudo(nomUtilisateur).getIdUtilisateur();
        List<Integer> lesIdEvent = facadeSalon.getEvenementUser(idUser);
        List<Evenement> lesEvents = new ArrayList<>();
        if(lesIdEvent.isEmpty()){
            throw new AucunEventException();
        }else{
            for(int idSalon : lesIdEvent){
                lesEvents.add(facadeSalon.getEventById(idSalon));
            }
        }
        return lesEvents;
    }

    @PostMapping("/utilisateur/{nomMembre}")
    public ResponseEntity<?> ajouterMembre(@PathVariable String nomMembre){
        try{
            facadeSalon.ajouterMembre(nomMembre);
            return ResponseEntity.ok().body("Membre ajouté");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Echec de l'ajout");
        }
    }



}
