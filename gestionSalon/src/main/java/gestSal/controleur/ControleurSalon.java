package gestSal.controleur;

import gestSal.facade.FacadeSalon;
import gestSal.facade.erreurs.NomSalonVideException;
import gestSal.facade.erreurs.NomTropCourtException;
import gestSal.facade.erreurs.NumSalonDejaExistantException;
import gestSal.modele.Salon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/salon")
public class ControleurSalon {

    @Autowired
    FacadeSalon facadeSalon;

    @PostMapping(value = "/creerSalon")
    public ResponseEntity<String> creerSalon(@RequestBody String nomCreateur, @RequestBody String nomSalon) {
        try {
            Salon salon = facadeSalon.creerSalon(nomCreateur, nomSalon);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/salon/{nomDuSalon}")
                    .buildAndExpand(nomSalon)
                    .toUri();
            //TODO AJOUT EN BDD
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, location.toString())
                    .body("Salon créé avec succès");
        } catch (NumSalonDejaExistantException | NomSalonVideException | NomTropCourtException e) {
            throw new RuntimeException(e);
        }
    }
}
