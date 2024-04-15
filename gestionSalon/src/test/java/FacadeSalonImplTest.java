import gestSal.facade.FacadeSalonImpl;
import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeSalonImplTest {

    private FacadeSalonImpl facadeSalon;

    @Before
    public void init() {
        facadeSalon = new FacadeSalonImpl();
    }

    //##################################CREER SALON####################################################
    @Test
    public void creerSalonOk() throws NumSalonDejaExistantException, NomSalonVideException, NomTropCourtException {
        Salon s = facadeSalon.creerSalon("sid", "SalonQuiMarche");
        assertNotNull(s);
        assertEquals("SalonQuiMarche", s.getNomSalon());
        assertEquals("sid", s.getNomCreateur());
    }

    @Test(expected = NomSalonVideException.class)
    public void creerSalonKoNomVide() throws NomSalonVideException, NomTropCourtException, NumSalonDejaExistantException {
        facadeSalon.creerSalon("createur", "");
    }

    @Test(expected = NomTropCourtException.class)
    public void creerSalonAvecNomTropCourt() throws NomSalonVideException, NomTropCourtException, NumSalonDejaExistantException {
        facadeSalon.creerSalon("romain", "IH");
    }

    //##################################REJOINDRE SALON####################################################
    @Test(expected = UtilisateurDejaPresentException.class)
    public void testRejoindreSalonAvecUtilisateurDejaPresent() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        salon.setListeMembre(new ArrayList<>());
        salon.getListeMembre().add(utilisateur);

        facadeSalon.rejoindreSalon(utilisateur, salon);
    }

    @Test
    public void testRejoindreSalonReussi() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        salon.setListeMembre(new ArrayList<>());

        facadeSalon.rejoindreSalon(utilisateur, salon);
        assertTrue(salon.getListeMembre().contains(utilisateur));
    }

    //##################################INVITER UTILISATEUR####################################################
    @Test
    public void testInviterUtilisateur() throws Exception {
        Salon salon = new Salon();
        Utilisateur utilisateur = new Utilisateur();

        String invitationUrl = facadeSalon.inviterUtilisateur(salon, utilisateur);
        assertNotNull(invitationUrl);
        assertTrue(invitationUrl.startsWith("https://evnt.com/invitation?code="));
        //TODO : a completer
    }

    //##################################Ajouter Moderateur####################################################
    @Test(expected = UtilisateurDejaModoException.class)
    public void testAjouterModerateurAuSalonAvecModoExistant() throws Exception {
        Utilisateur nouveauModo = new Utilisateur();
        Salon salon = new Salon();
        salon.setListeModerateur(new ArrayList<>());
        salon.getListeModerateur().add(nouveauModo);

        facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon);
    }

    @Test
    public void testAjouterModerateurAuSalonOk() throws Exception {
        Utilisateur nouveauModo = new Utilisateur();
        Salon salon = new Salon();
        salon.setListeModerateur(new ArrayList<>());

        facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon);
        assertTrue(salon.getListeModerateur().contains(nouveauModo));
    }

    //################################## Retirer Moderateur####################################################
    @Test(expected = UtilisateurPasModoException.class)
    public void testRetirerModerateurDuSalonKoSansEtreModo() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        salon.setListeModerateur(new ArrayList<>());

        facadeSalon.retirerModerateurDuSalon(salon, utilisateur);
    }

    @Test
    public void testRetirerModerateurDuSalonReussi() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        salon.setListeModerateur(new ArrayList<>());
        salon.getListeModerateur().add(utilisateur);

        facadeSalon.retirerModerateurDuSalon(salon, utilisateur);
        assertFalse(salon.getListeModerateur().contains(utilisateur));
    }

    //##################################CREER EVENEMENT####################################################
    @Test
    public void creerEvenementOk() throws NomEvenementDejaPrisException, NomEvenementVideException, SalonInexistantException {
        Salon salon = new Salon();
        Utilisateur createur = new Utilisateur();
        facadeSalon.salons.add(salon);

        String nomEvenement = "EventSuccess";
        Evenement event = facadeSalon.creerEvenement(salon, nomEvenement, 100, "Détails de l'événement", "Localisation", createur, "2024-04-14");
        assertNotNull(event);
        assertEquals(nomEvenement, event.getNomEvenement());
    }

    @Test(expected = NomEvenementVideException.class)
    public void creerEvenementKoNomVide() throws NomEvenementDejaPrisException, NomEvenementVideException, SalonInexistantException {
        Salon salon = new Salon();
        Utilisateur createur = new Utilisateur();
        facadeSalon.salons.add(salon);

        facadeSalon.creerEvenement(salon, "", 100, "Détails de l'événement", "Localisation", createur, "2024-04-14");
    }

    @Test(expected = NomEvenementDejaPrisException.class)
    public void creerEvenementKoNomDejaPris() throws NomEvenementDejaPrisException, NomEvenementVideException, SalonInexistantException {
        Salon salon = new Salon();
        Utilisateur createur = new Utilisateur();
        facadeSalon.salons.add(salon);

        String nomEvenement = "EventExists";
        Evenement existingEvent = new Evenement();
        existingEvent.setNomEvenement(nomEvenement);
        facadeSalon.evenements.add(existingEvent);

        facadeSalon.creerEvenement(salon, nomEvenement, 100, "Détails de l'événement", "Localisation", createur, "2024-04-14");
    }

    //##################################TEST PRESENT EVENEMENT ####################################################

    @Test
    public void testSeDefiniCommePresentAUnEvenementOk() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setListeParticipants(new ArrayList<>());
        salon.setLesEvenements(new ArrayList<>());
        salon.setListeMembre(new ArrayList<>());
        salon.getLesEvenements().add(evenement);
        salon.getListeMembre().add(utilisateur);
        facadeSalon.salons.add(salon);

        List<Utilisateur> participants = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);
        assertTrue(participants.contains(utilisateur));
    }

    @Test(expected = UtilisateurInexistantException.class)
    public void testSeDefiniCommePresentAUnEvenementKoUtilisateurInexistant() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setListeParticipants(new ArrayList<>());
        salon.setListeMembre(new ArrayList<>());
        salon.setLesEvenements(new ArrayList<>());
        salon.getLesEvenements().add(evenement);
        facadeSalon.salons.add(salon);

        facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);
    }


    //##################################TEST ABSENT EVENEMENT ####################################################


    @Test
    public void testSeDefiniCommeAbsentAUnEvenementOk() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setListeParticipants(new ArrayList<>());
        evenement.getListeParticipants().add(utilisateur);
        salon.setLesEvenements(new ArrayList<>());
        salon.setListeMembre(new ArrayList<>());
        salon.getLesEvenements().add(evenement);
        salon.getListeMembre().add(utilisateur);
        facadeSalon.evenements.add(evenement);
        facadeSalon.salons.add(salon);

        List<Utilisateur> participants = facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);
        assertFalse(participants.contains(utilisateur));
    }

    @Test(expected = UtilisateurInexistantException.class)
    public void testSeDefiniCommeAbsentAUnEvenementKoUtilisateurInexistant() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setListeParticipants(new ArrayList<>());
        salon.setLesEvenements(new ArrayList<>());
        salon.setListeMembre(new ArrayList<>());
        salon.getLesEvenements().add(evenement);
        facadeSalon.evenements.add(evenement);
        facadeSalon.salons.add(salon);

        facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);
    }

    @Test(expected = EvenementInexistantException.class)
    public void testSeDefiniCommeAbsentEvenementKoEvenementInexistant() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        salon.setLesEvenements(new ArrayList<>());
        salon.setListeMembre(new ArrayList<>());
        salon.getLesEvenements().add(evenement);
        facadeSalon.salons.add(salon);

        facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);
    }


    //################################## GET UTILISATEUR PAR PSEUDO ####################################################

    @Test
    public void testGetUtilisateurByPseudoOk() throws NomUtilisateurVideException, UtilisateurInexistantException {
        Utilisateur expectedUser = new Utilisateur();
        expectedUser.setPseudo("testUser");
        facadeSalon.utilisateurs.put("testUser", expectedUser);

        Utilisateur result = facadeSalon.getUtilisateurByPseudo("testUser");
        assertNotNull(result);
        assertEquals("testUser", result.getPseudo());
    }

    @Test(expected = NomUtilisateurVideException.class)
    public void testGetUtilisateurByPseudoVide() throws NomUtilisateurVideException, UtilisateurInexistantException {
        facadeSalon.getUtilisateurByPseudo("");
    }

    @Test(expected = UtilisateurInexistantException.class)
    public void testGetUtilisateurByPseudoInexistant() throws NomUtilisateurVideException, UtilisateurInexistantException {
        facadeSalon.getUtilisateurByPseudo("inexistant");
    }

    //################################## GET SALON BY NUM ####################################################


    @Test
    public void testGetSalonByNumOk() throws SalonInexistantException {
        Salon expectedSalon = new Salon();
        expectedSalon.setNumSalon(1234);
        facadeSalon.salons.add(expectedSalon);

        Salon result = facadeSalon.getSalonByNum(1234);
        assertNotNull(result);
        assertEquals(1234, result.getNumSalon());
    }

    @Test(expected = SalonInexistantException.class)
    public void testGetSalonByNumInexistant() throws SalonInexistantException {
        facadeSalon.getSalonByNum(9999);
    }

    //################################## GET SALON BY NOM ####################################################

    @Test
    public void testGetSalonByNomOk() throws SalonInexistantException, NomSalonVideException {
        Salon expectedSalon = new Salon();
        expectedSalon.setNomSalon("testSalon");
        facadeSalon.salons.add(expectedSalon);

        Salon result = facadeSalon.getSalonByNom("testSalon");
        assertNotNull(result);
        assertEquals("testSalon", result.getNomSalon());
    }

    @Test(expected = NomSalonVideException.class)
    public void testGetSalonByNomVide() throws SalonInexistantException, NomSalonVideException {
        facadeSalon.getSalonByNom("");
    }

    @Test(expected = SalonInexistantException.class)
    public void testGetSalonByNomInexistant() throws SalonInexistantException, NomSalonVideException {
        facadeSalon.getSalonByNom("inexistant");
    }



    //################################## VALIDER EVENEMENT ####################################################

    @Test
    public void testValiderEvenementOk() throws EvenementInexistantException {
        Evenement evenement = new Evenement();
        evenement.setNombrePersonneMax(10);
        evenement.setListeParticipants(new ArrayList<>());
        for (int i = 0; i < 10; i++) {
            evenement.getListeParticipants().add(new Utilisateur());
        }
        facadeSalon.evenements.add(evenement);

        boolean result = facadeSalon.validerEvenement(evenement);
        assertTrue(result);
        assertTrue(evenement.isEstValide());
    }


    @Test(expected = EvenementInexistantException.class)
    public void testValiderEvenementInexistant() throws EvenementInexistantException {
        Evenement evenement = new Evenement();
        facadeSalon.validerEvenement(evenement);
    }

    //################################## MODIFIER SALON ####################################################

    @Test
    public void testModifierSalonNomOk() throws Exception {
        Salon salon = new Salon();
        salon.setNomSalon("AncienNom");
        facadeSalon.salons.add(salon);

        facadeSalon.modifierSalon(salon, "nom", "NouveauNom");
        assertEquals("NouveauNom", salon.getNomSalon());
    }

    //################################## GET EVENEMENT NOM ET NUM SALON ####################################################

    @Test
    public void testGetEvenementByNomEtNumSalonOk() throws Exception {
        Salon salon = new Salon();
        salon.setNumSalon(123);
        Evenement evenement = new Evenement();
        evenement.setNomEvenement("EventTest");
        salon.setLesEvenements(new ArrayList<>());
        salon.getLesEvenements().add(evenement);
        facadeSalon.salons.add(salon);

        Evenement result = facadeSalon.getEvenementByNomEtNumSalon(123, "EventTest");
        assertNotNull(result);
        assertEquals("EventTest", result.getNomEvenement());
    }

    @Test(expected = EvenementInexistantException.class)
    public void testGetEvenementByNomEtNumSalonInexistant() throws Exception {
        facadeSalon.getEvenementByNomEtNumSalon(123, "EventInexistant");
    }

    //################################## MODIFIER EVENEMENT  ####################################################

    @Test
    public void testModifierEvenementDescriptionOk() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setDetailsEvenement("Ancienne description");
        facadeSalon.evenements.add(evenement);

        facadeSalon.modifierEvenement(evenement, "description", "Nouvelle description");
        assertEquals("Nouvelle description", evenement.getDetailsEvenement());
    }

    @Test
    public void testModifierEvenementNomOk() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setNomEvenement("AncienNom");
        facadeSalon.evenements.add(evenement);

        facadeSalon.modifierEvenement(evenement, "nom", "NouveauNom");
        assertEquals("NouveauNom", evenement.getNomEvenement());
    }

    @Test(expected = EvenementInexistantException.class)
    public void testModifierEvenementInexistant() throws Exception {
        Evenement evenement = new Evenement();
        facadeSalon.modifierEvenement(evenement, "description", "Nouvelle description");
    }

    //################################## VALIDER EVENEMENT  ####################################################

    @Test
    public void testValiderEvenementNonValide() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setNombrePersonneMax(10);
        evenement.setListeParticipants(new ArrayList<>());
        facadeSalon.evenements.add(evenement);

        boolean result = facadeSalon.validerEvenement(evenement);
        assertFalse(result);
        assertFalse(evenement.isEstValide());
    }

    //################################## ENVOYER MESSAGE SALON  ####################################################

    @Test
    public void envoyerMessageSalonOk() throws Exception {
        Salon salon = new Salon();
        salon.setConversation(new ArrayList<>());
        facadeSalon.salons.add(salon);
        facadeSalon.utilisateurs.put("testUser", new Utilisateur());

        facadeSalon.envoyerMessageSalon(salon, "testUser", "Bonjour tout le monde");
        assertFalse(salon.getConversation().isEmpty());
    }

    @Test(expected = SalonInexistantException.class)
    public void envoyerMessageSalonKoSalonInexistant() throws Exception {
        Salon salon = new Salon();
        facadeSalon.envoyerMessageSalon(salon, "testUser", "Message");
    }

    @Test(expected = UtilisateurInexistantException.class)
    public void envoyerMessageSalonKoUtilisateurInexistant() throws Exception {
        Salon salon = new Salon();
        salon.setConversation(new ArrayList<>());
        facadeSalon.salons.add(salon);

        facadeSalon.envoyerMessageSalon(salon, "userInexistant", "Message");
    }

    //################################## ENVOYER MESSAGE EVENEMENT  ####################################################

    @Test
    public void envoyerMessageEvenementOk() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setConversation(new ArrayList<>());
        facadeSalon.evenements.add(evenement);
        facadeSalon.utilisateurs.put("testUser", new Utilisateur());

        facadeSalon.envoyerMessageEvenement(evenement, "testUser", "Salut");
        assertFalse(evenement.getConversation().isEmpty());
    }

    @Test(expected = EvenementInexistantException.class)
    public void envoyerMessageEvenementKoEvenementInexistant() throws Exception {
        Evenement evenement = new Evenement();
        facadeSalon.envoyerMessageEvenement(evenement, "testUser", "Message");
    }

    @Test(expected = UtilisateurInexistantException.class)
    public void envoyerMessageEvenementKoUtilisateurInexistant() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setConversation(new ArrayList<>());
        facadeSalon.evenements.add(evenement);

        facadeSalon.envoyerMessageEvenement(evenement, "userInexistant", "Message");
    }
    //################################## GET MESSAGES SALONS  ####################################################

    @Test
    public void getMessagesSalonOk() throws Exception {
        Salon salon = new Salon();
        salon.setConversation(new ArrayList<>());
        facadeSalon.salons.add(salon);

        List<Message> messages = facadeSalon.getMessagesSalon(salon);
        assertNotNull(messages);
    }

    @Test(expected = SalonInexistantException.class)
    public void getMessagesSalonKoSalonInexistant() throws Exception {
        Salon salon = new Salon();
        facadeSalon.getMessagesSalon(salon);
    }
    //################################## GET MESSAGES EVENEMENTS  ####################################################

    @Test
    public void getMessagesEvenementOk() throws Exception {
        Evenement evenement = new Evenement();
        evenement.setConversation(new ArrayList<>());
        facadeSalon.evenements.add(evenement);

        List<Message> messages = facadeSalon.getMessagesEvenement(evenement);
        assertNotNull(messages);
    }

    @Test(expected = EvenementInexistantException.class)
    public void getMessagesEvenementKoEvenementInexistant() throws Exception {
        Evenement evenement = new Evenement();
        facadeSalon.getMessagesEvenement(evenement);
    }
}
