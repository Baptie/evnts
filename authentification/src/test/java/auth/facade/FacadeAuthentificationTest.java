package auth.facade;

import auth.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeAuthentificationTest {

    private FacadeAuthentificationInterface facadeAuth;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        facadeAuth = new FacadeAuthentificationImpl(passwordEncoder);
    }
    @Test
    public void inscription_Successful() {
        assertDoesNotThrow(() -> facadeAuth.inscription("Romain", "coucou", "romain@test.com"));
        assertTrue(facadeAuth.getUtilisateurs().containsKey("Romain"));
    }

    @Test
    public void inscription_PseudoAlreadyTaken() {
        assertThrows(PseudoDejaPrisException.class, () -> {
            facadeAuth.inscription("Vincent", "salut", "Vincent@test.com");
            facadeAuth.inscription("Vincent", "coucou", "Baptiste@test.com");
        });
    }

    @Test
    public void inscription_EmailAlreadyTaken() {
        assertThrows(EMailDejaPrisException.class, () -> {
            facadeAuth.inscription("Baptiste", "coucou", "baptiste@test.com");
            facadeAuth.inscription("Sidney", "salut", "baptiste@test.com");
        });
    }

    @Test
    public void connexion_Successful() throws Exception {
        facadeAuth.inscription("Sidney", "pass", "sid@test.com");
        String token = facadeAuth.connexion("Sidney", "pass");
        assertNotNull(token);
        assertTrue(facadeAuth.getStatus("Sidney"));
    }

    @Test
    public void connexion_UserNotFound() {
        assertThrows(UtilisateurInexistantException.class, () -> facadeAuth.connexion("Axelle", "Axelle45"));
    }

    @Test
    public void connexion_IncorrectPassword() {
        assertThrows(MdpIncorrecteException.class, () -> {
            facadeAuth.inscription("toto", "leBonMDP", "toto@test.com");
            facadeAuth.connexion("toto", "leMauvaisMDP");
        });
    }

    @Test
    public void deconnexion_Successful() throws Exception {
        facadeAuth.inscription("Sidney", "pass", "sid@test.com");
        String token = facadeAuth.connexion("Sidney", "pass");
        assertNotNull(token);
        assertTrue(facadeAuth.getStatus("Sidney"));

        facadeAuth.deconnexion("Sidney");
        assertFalse(facadeAuth.getStatus("Sidney"));
    }

    @Test
    public void deconnexion_UserNotFound() {
        assertThrows(UtilisateurInexistantException.class, () -> facadeAuth.deconnexion("Axelle"));
    }

    @Test
    public void reSetPseudo_Successful() throws Exception {
        String ancienPseudo = "Cristiano";
        facadeAuth.inscription(ancienPseudo, "ronaldo", "Cristiano@test.com");

        String nouveauPseudo = "Lionel";
        facadeAuth.reSetPseudo(ancienPseudo, nouveauPseudo);

        assertTrue(facadeAuth.getUtilisateurs().containsKey(nouveauPseudo));
        assertFalse(facadeAuth.getUtilisateurs().containsKey(ancienPseudo));
    }

    @Test
    public void reSetPseudo_UserNotFound() {
        assertThrows(UtilisateurInexistantException.class, () -> facadeAuth.reSetPseudo("Pen", "Gu"));
    }

    @Test
    public void reSetMDP_Successful() throws Exception {
        String ancienMDP = "pandemic";
        facadeAuth.inscription("Cedric", "pandemic", "Cedric@example.com");

        String nouveauMDP = "demicpan";
        facadeAuth.reSetMDP("Cedric",ancienMDP, nouveauMDP);

        String token = facadeAuth.connexion("Cedric", nouveauMDP);
        assertNotNull(token);
    }

    @Test
    public void reSetMDP_UserNotFound() {
        assertThrows(UtilisateurInexistantException.class, () -> facadeAuth.reSetMDP("ML", "blabla","FrontBof"));
    }

    @Test
    public void supprimerUtilisateur_Successful() throws Exception {
        facadeAuth.inscription("user1", "password1", "user1@example.com");
        facadeAuth.supprimerUtilisateur("user1", "password1");
        assertFalse(facadeAuth.getUtilisateurs().containsKey("user1"));
    }

    @Test
    public void supprimerUtilisateur_UserNotFound() {
        assertThrows(UtilisateurInexistantException.class, () -> facadeAuth.supprimerUtilisateur("user2", "password2"));
    }

    @Test
    public void supprimerUtilisateur_IncorrectPassword() {
        assertThrows(MdpIncorrecteException.class, () -> {
            // Inscrire un utilisateur
            facadeAuth.inscription("user3", "password3", "user3@example.com");

            // Supprimer l'utilisateur avec un mot de passe incorrect
            facadeAuth.supprimerUtilisateur("user3", "wrongPassword");
        });
    }
}
