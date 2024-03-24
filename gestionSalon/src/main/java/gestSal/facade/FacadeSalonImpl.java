package gestSal.facade;

import gestSal.dto.*;
import gestSal.facade.erreurs.*;
import gestSal.modele.*;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component("facadeSalon")
public class FacadeSalonImpl implements FacadeSalon {

    public Map<String, Utilisateur> utilisateurs;

    public List<Salon> salons;

    public List<Evenement> evenements;

    public List<Conversation> conversations;

    public FacadeSalonImpl(){
        this.utilisateurs=new HashMap<>();
        this.salons = new ArrayList<>();
        this.evenements = new ArrayList<>();
        this.conversations=new ArrayList<>();
    }

    @Override
    public Salon creerSalon(String nomCreateur, String nomSalon) throws NomSalonVideException, NomTropCourtException, NumSalonDejaExistantException {
        Salon salonCree = null;
        if(nomSalon.isBlank()){
            throw new NomSalonVideException();
        }else if(nomSalon.length()<=3){
            throw new NomTropCourtException();
        }else{
            int id = generateRandom4DigitNumber();
            salonCree = new Salon();
            salonCree.setNomSalon(nomSalon);
            salonCree.setNomCreateur(nomCreateur);
            salonCree.setNumSalon(id);
        }

        return salonCree;
    }


    @Override
    public Salon modifierSalon(Salon salon, String choix, String valeur) throws SalonInexistantException, NomSalonVideException, NumeroSalonVideException {
        switch (choix) {
//            case "num" -> salon.setNumSalon(Integer.parseInt(valeur));
            case "nom" -> salon.setNomSalon(valeur);
            case "logo" -> salon.setLogo(valeur);
            case "createur" -> salon.setNomCreateur(valeur);
        }
        return salon;
    }

    @Override
    public void rejoindreSalon(Utilisateur utilisateur, Salon salonRejoint) throws SalonInexistantException, NomSalonVideException, UtilisateurInexistantException, NomUtilisateurVideException,UtilisateurDejaPresentException {
        if(!salonRejoint.getListeMembre().contains(utilisateur)){
            salonRejoint.getListeMembre().add(utilisateur);
        }else{
            throw new UtilisateurDejaPresentException();
        }
    }

    @Override
    public String inviterUtilisateur(Salon salonInvite, Utilisateur utilisateurInvite) throws SalonInexistantException, UtilisateurInexistantException, NomSalonVideException, NomUtilisateurVideException {

        String baseUrl = "https://evnt.com/invitation?code=";
        String invitationCode = generateRandomCode(8); // Vous pouvez ajuster la longueur du code ici
        String invitationUrl = baseUrl + invitationCode;
        //TODO ENVOIE DE LA NOTIF A LA PERSONNE, MAIL ?
        return invitationUrl;

    }

    @Override
    public Utilisateur getUtilisateurByPseudo(String pseudoUtilisateur) throws NomUtilisateurVideException,UtilisateurInexistantException {
        if(pseudoUtilisateur.isEmpty()){
            throw new NomUtilisateurVideException()
                    ;
        }else if(utilisateurs.containsKey(pseudoUtilisateur)){
            return utilisateurs.get(pseudoUtilisateur);
        }else{
            throw new UtilisateurInexistantException();
        }
    }

    @Override
    public Salon getSalonByNum(int numSalon) throws SalonInexistantException {
        for(Salon s : salons){
            if(s.getNumSalon()==numSalon){
                return s;
            }
        }
        throw new SalonInexistantException();
    }

    @Override
    public Salon getSalonByNom(String nomSalon) throws SalonInexistantException,NomSalonVideException {
        if(nomSalon.isBlank()){
            throw new NomSalonVideException();
        }
        for(Salon s : salons){
            if(s.getNomSalon()==nomSalon){
                return s;
            }
        }
        throw new SalonInexistantException();
    }

    @Override
    public void retirerModerateurDuSalon(Salon salon, Utilisateur utilisateurPlusModo) throws NomSalonVideException, NomUtilisateurVideException,UtilisateurPasModoException {
        if(salon.getListeModerateur().contains(utilisateurPlusModo)){
            salon.getListeModerateur().remove(utilisateurPlusModo);
        }else{
            throw new UtilisateurPasModoException();
        }
    }

    @Override
    public void ajouterModerateurAuSalon(Utilisateur nouveauModo, Salon salonPourLeNouveauModo) throws NomUtilisateurVideException, NomSalonVideException, PasAdminException, UtilisateurDejaModoException, SalonInexistantException {
        if(salons.contains(salonPourLeNouveauModo)){
            throw new SalonInexistantException();
        }

        if(!salonPourLeNouveauModo.getListeModerateur().contains(nouveauModo)){
            salonPourLeNouveauModo.getListeModerateur().add(nouveauModo);
        }else{
            throw new UtilisateurDejaModoException();
        }
    }

    @Override
    public List<Utilisateur> seDefiniCommePresentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws UtilisateurInexistantException, SalonInexistantException, EvenementInexistantException {
        if(!salons.contains(salon)){
            throw new SalonInexistantException();
        }
        List<Evenement> lesEvenements = salon.getLesEvenements();
        if(!salon.getListeMembre().contains(utilisateur)){
            throw new UtilisateurInexistantException();
        }else if(!lesEvenements.contains(evenement)){
            throw new EvenementInexistantException();
        }
        if(lesEvenements.contains(evenement)){
            evenement.getListeParticipants().add(utilisateur);
        }
        return evenement.getListeParticipants();
    }

    @Override
    public List<Utilisateur> seDefiniCommeAbsentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement) throws UtilisateurInexistantException, SalonInexistantException, EvenementInexistantException {
        if(!evenements.contains(evenement)){
            throw new EvenementInexistantException();
        }
        if(!salons.contains(salon)){
            throw new SalonInexistantException();
        }

        List<Evenement> lesEvenements = salon.getLesEvenements();
        if(lesEvenements.contains(evenement)){
            if(evenement.getListeParticipants().contains(utilisateur)){
                evenement.getListeParticipants().remove(utilisateur);
            }else{
                throw new UtilisateurInexistantException();
            }
            return evenement.getListeParticipants();
        }
        throw new EvenementInexistantException();
    }

    @Override
    public Evenement getEvenementByNomEtNumSalon(int numSalon, String nomEvenement) throws EvenementInexistantException {
        for(Salon s : salons){ //parcours des salons
            if(s.getNumSalon()==numSalon){ // si bon num salon on regarde ses evenements
                for(Evenement e : s.getLesEvenements()){ //parcours evenement
                    if(e.getNomEvenement()==nomEvenement){ // si bon nom = bon evenement
                        return e;
                    }
                }
            }
        }
        throw new EvenementInexistantException();
    }

    @Override
    public Evenement creerEvenement(String nomEvenement) throws NomEvenementDejaPrisException, NomEvenementVideException, SalonInexistantException {
        if(evenements.contains(nomEvenement)){
            throw new NomEvenementDejaPrisException();
        }
        Evenement event = null;
        if(nomEvenement.isBlank()){
            throw new NomEvenementVideException();
        }else{
            if(checkSiNomEvenementDejaPris(nomEvenement)){
                throw new NomEvenementDejaPrisException();
            }else{
                event = new Evenement();
                event.setNomCreateur(nomEvenement);
            }
        }
        return event;
    }

    private boolean checkSiNomEvenementDejaPris(String nomEvenement){
        for(Evenement e : evenements){
            if(e.getNomEvenement()==nomEvenement){
                return true;
            }
        }
        return false;
    }

    private boolean checkSiNomEvenementDejaPris(Salon salon, String nomEvenement) throws SalonInexistantException {
        if(!salons.contains(salon)){
            throw new SalonInexistantException();
        }

        for(Evenement e : salon.getLesEvenements()){
            if(e.getNomEvenement()==nomEvenement){
                return true;
            }
        }
        return false;
    }

    public UtilisateurDTO convertUtilisateurToDTO(Utilisateur utilisateur){
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
        utilisateurDTO.setPseudo(utilisateur.getPseudo());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setDescription(utilisateur.getDescription());
        utilisateurDTO.setStatus(utilisateur.getStatus());
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setMesConversations(utilisateur.getMesConversations());
        return utilisateurDTO;
    }
    public SalonDTO convertSalonToDTO(Salon salon){
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setIdSalon(salon.getIdSalon());
        salonDTO.setNumSalon(salon.getNumSalon());
        salonDTO.setNomSalon(salon.getNomSalon());
        salonDTO.setNomCreateur(salon.getNomCreateur());
        salonDTO.setLogo(salon.getLogo());
        salonDTO.setListeMembre(salon.getListeMembre());
        salonDTO.setListeModerateur(salon.getListeModerateur());
        salonDTO.setConversation(salon.getConversation());
        salonDTO.setLesEvenements(salon.getLesEvenements());
        return salonDTO;
    }

    public EvenementDTO convertEvenementToDTO(Evenement event){
        EvenementDTO evenementDTO = new EvenementDTO();
        evenementDTO.setIdEvenement(event.getIdEvenement());
        evenementDTO.setNombrePersonneMax(event.getNombrePersonneMax());
        evenementDTO.setNomEvenement(event.getNomEvenement());
        evenementDTO.setDetailsEvenement(event.getDetailsEvenement());
        evenementDTO.setLieu(event.getLieu());
        evenementDTO.setNomCreateur(event.getNomCreateur());
        evenementDTO.setListeParticipants(event.getListeParticipants());
        evenementDTO.setDate(event.getDate());
        evenementDTO.setEstValide(event.isEstValide());
        evenementDTO.setEstTermine(event.isEstTermine());
        evenementDTO.setConversation(event.getConversation());

        return evenementDTO;

    }

    public MessageDTO convertMessageToDTO(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setIdMessage(message.getIdMessage());
        messageDTO.setAuteur(message.getAuteur());
        messageDTO.setReceveur(message.getReceveur());
        messageDTO.setContenu(message.getContenu());
        messageDTO.setDate(message.getDate());
        messageDTO.setSeen(message.isSeen());
        return messageDTO;
    }
    public ConversationDTO convertConversationToDto(Conversation conversation){
        ConversationDTO convDto = new ConversationDTO();
        convDto.setIdConversation(conversation.getIdConversation());
        convDto.setUtilisateurUn(conversation.getUtilisateurUn());
        convDto.setUtilisateurDeux(conversation.getUtilisateurDeux());
        convDto.setLesMessagesDeLaConversation(conversation.getLesMessagesDeLaConversation());
        return convDto;
    }

    @Override
    public Evenement modifierEvenement(Evenement evenement, String choix, String valeur) throws EvenementInexistantException {
        if(!evenements.contains(evenement)){
            throw new EvenementInexistantException();
        }
        switch (choix) {
            case "description" -> evenement.setDetailsEvenement(valeur);
            case "date" -> evenement.setDate(Date.valueOf(valeur));
            case "lieu" -> evenement.setLieu(valeur);
            case "nombre" -> evenement.setNombrePersonneMax(Integer.parseInt(valeur));
            case "nom" -> evenement.setNomEvenement(valeur);
        }
        return evenement;
    }

    @Override
    public boolean validerEvenement(Evenement evenement) throws EvenementInexistantException {
        if(!evenements.contains(evenement)){
            throw new EvenementInexistantException();
        }

        boolean isValide;
        if(evenement.getListeParticipants().size()==evenement.getNombrePersonneMax()){
            evenement.setEstValide(true);
            isValide = true;
        }else{
            evenement.setEstValide(false);
            isValide = false;
        }
        return isValide;
    }

    @Override
    public void envoyerMessageSalon(Salon salon, String pseudoUtilisateur, String contenu) throws SalonInexistantException, UtilisateurInexistantException {
        if(!salons.contains(salon)){
            throw new SalonInexistantException();
        }if(!utilisateurs.containsKey(pseudoUtilisateur)){
            throw new UtilisateurInexistantException();
        }
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        // 2017-12-24 T 04:34:27.0871036
        salon.getConversation().add(new Message(generateRandom4DigitNumber()+generateRandom4DigitNumber(),pseudoUtilisateur,salon.getNomSalon(),contenu,dateTime,false));
    }

    @Override
    public void envoyerMessageEvenement(Evenement evenement, String pseudoUtilisateur, String contenu) throws EvenementInexistantException,UtilisateurInexistantException{
        if(!evenements.contains(evenement)){
            throw new EvenementInexistantException();
        }if(!utilisateurs.containsKey(pseudoUtilisateur)){
            throw new UtilisateurInexistantException();
        }
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        evenement.getConversation().add(new Message(generateRandom4DigitNumber()+generateRandom4DigitNumber(),pseudoUtilisateur,evenement.getNomEvenement(),contenu,dateTime,false));
    }


    @Override
    public List<Message> getMessagesSalon(Salon salon) throws SalonInexistantException{
        if(!salons.contains(salon)){
            throw new SalonInexistantException();
        }

        return salon.getConversation();
    }
    @Override
    public List<Message> getMessagesEvenement(Evenement evenement) throws EvenementInexistantException{
        if(!evenements.contains(evenement)){
            throw new EvenementInexistantException();
        }

        return evenement.getConversation();
    }

    @Override
    public Utilisateur convertUserDTOtoUser(UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo(utilisateurDTO.getPseudo());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        utilisateur.setDescription(utilisateurDTO.getDescription());
        utilisateur.setStatus(utilisateurDTO.getStatus());
        utilisateur.setMesConversations(utilisateurDTO.getMesConversations());
        return utilisateur;
    }


    private String generateRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomValue = secureRandom.nextInt('Z' - 'A' + 1) + 'A';
            randomCode.append((char) randomValue);
        }

        return randomCode.toString();
    }
    private int generateRandom4DigitNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    public Statement connecterAuSalonSQL() throws SQLException {
        // Connexion à la base de données MySQL
        String jdbcUrl = "jdbc:mysql://localhost:3307/salon";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        Statement statement = connection.createStatement();
        return statement;

    }

    public void creerSalonSQL(String nomSalon, String nomCreateur, String logo) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO Salon (nomSalon, nomCreateur, logo) VALUES ('"+nomSalon+"', '"+nomCreateur+"', '"+logo+"')";
        st.executeUpdate(SQL);
    }

}
