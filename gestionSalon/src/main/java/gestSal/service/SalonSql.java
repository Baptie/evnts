package gestSal.service;

import gestSal.dto.EvenementDTO;
import gestSal.dto.MessageDTO;
import gestSal.dto.SalonDTO;
import gestSal.dto.UtilisateurDTO;
import gestSal.facade.FacadeSalon;
import gestSal.facade.FacadeSalonImpl;
import gestSal.modele.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalonSql {

    static FacadeSalon facadeSalon = new FacadeSalonImpl();

    public SalonSql() {
    }

    public static Statement connecterAuSalonSQL() throws SQLException {
        // Connexion à la base de données MySQL
        String jdbcUrl = "jdbc:mysql://localhost:3308/utilisateur";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        Statement statement = connection.createStatement();
        return statement;

    }

    public static void creerSalonSQL(String nomSalon, String nomCreateur, String logo) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO Salon (nomSalon, nomCreateur, logo) VALUES ('"+nomSalon+"', '"+nomCreateur+"', '"+logo+"')";
        st.executeUpdate(SQL);
    }

    public static SalonDTO getSalonById(int id) throws SQLException {
        SalonDTO salon = new SalonDTO();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Salon where idSalon="+id;
        ResultSet rs = st.executeQuery(SQL);
        while(rs.next()){
            salon.setIdSalon(rs.getInt("idSalon"));
            salon.setNomSalon(rs.getString("nomSalon"));
            salon.setNomCreateur(rs.getString("nomCreateur"));
            salon.setLogo(rs.getString("logo"));
        }
        return salon;

    }

    public static SalonDTO getSalonByName(String nom) throws SQLException {
        SalonDTO salon = new SalonDTO();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Salon where nomSalon='"+nom+"'";
        ResultSet rs = st.executeQuery(SQL);
        while(rs.next()){
            salon.setIdSalon(rs.getInt("idSalon"));
            salon.setNomSalon(rs.getString("nomSalon"));
            salon.setNomCreateur(rs.getString("nomCreateur"));
            salon.setLogo(rs.getString("logo"));
        }
        return salon;

    }

    public static SalonDTO modifierSalonSQL(SalonDTO salonDTO, String choix, String valeur,int id) throws SQLException {
        Statement st = connecterAuSalonSQL();
        switch (choix){
            case "nom" -> {
                salonDTO.setNomSalon(valeur);
                String SQL = "UPDATE Salon SET nomSalon = '" + valeur + "' WHERE idSalon = " + id;
                st.executeUpdate(SQL);
            }
            case "logo" -> {
                salonDTO.setLogo(valeur);
                String SQL = "UPDATE Salon SET logo = '" + valeur + "' WHERE idSalon = " + id;
                st.executeUpdate(SQL);
            }
            case "createur" -> {
                salonDTO.setNomCreateur(valeur);
                String SQL = "UPDATE Salon SET nomCreateur = '" + valeur + "' WHERE idSalon = " + id;
                st.executeUpdate(SQL);
            }
        }
        return salonDTO;
    }


    public static UtilisateurDTO getUtilisateurByPseudoSQL(String pseudoUtilisateur) throws SQLException {
        UtilisateurDTO userDTO = new UtilisateurDTO();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Membre where nomMembre='"+pseudoUtilisateur+"'";
        ResultSet rs = st.executeQuery(SQL);
        while(rs.next()){
            userDTO.setIdUtilisateur(rs.getInt("idMembre"));
            userDTO.setPseudo(rs.getString("nomMembre"));
        }

        return userDTO;
    }

    public static UtilisateurDTO getUtilisateurByIdSQL(int idUtilisateur) throws SQLException {
        UtilisateurDTO userDTO = new UtilisateurDTO();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Membre where idMembre="+idUtilisateur;
        ResultSet rs = st.executeQuery(SQL);
        while(rs.next()){
            userDTO.setIdUtilisateur(rs.getInt("idMembre"));
            userDTO.setPseudo(rs.getString("nomMembre"));
        }

        return userDTO;
    }

    public static void rejoindreSalonSql(UtilisateurDTO utilisateurDTO, SalonDTO salonDTO) throws SQLException {
        int idUser = utilisateurDTO.getIdUtilisateur();
        int idSalon = salonDTO.getIdSalon();
        Statement st = connecterAuSalonSQL();
        String SQL = "insert into SalonMembre(idSalon, idMembre) VALUES ("+idSalon+", "+idUser+")";
        st.executeUpdate(SQL);

    }

    public static EvenementDTO getEvenementByNomEtNumSalonSQL(int id, String nomEvenement) throws SQLException {
        EvenementDTO evenementDTO = new EvenementDTO();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from Evenement where nomEvenement='"+nomEvenement+"' and idSalon ="+id;
        ResultSet rs = st.executeQuery(SQL);
        while (rs.next()) {
            evenementDTO.setIdEvenement(rs.getInt("idEvenement"));
            evenementDTO.setNombrePersonneMax(rs.getInt("nombrePersonneMax"));
            evenementDTO.setNomEvenement(rs.getString("nomEvenement"));
            evenementDTO.setDetailsEvenement(rs.getString("details"));
            evenementDTO.setDate(rs.getString("dateEvenement"));
            evenementDTO.setLieu(rs.getString("lieu"));
            evenementDTO.setEstValide(rs.getBoolean("isValide"));
            evenementDTO.setNomCreateur(rs.getString("nomCreateur"));

        }
        return evenementDTO;
    }

    public static EvenementDTO modifierEvenementSQL(EvenementDTO evenementDTO, String choix, String valeur) throws SQLException {
        Statement st = connecterAuSalonSQL();
        switch (choix) {
            case "description" -> {
                evenementDTO.setDetailsEvenement(valeur);
                String SQL = "UPDATE Evenement SET details = '" + valeur + "' WHERE idEvenement = " + evenementDTO.getIdEvenement();
                st.executeUpdate(SQL);
            }
            case "date" -> {
                evenementDTO.setDate(valeur);
                String SQL = "UPDATE Evenement SET dateEvenement = '" + valeur + "' WHERE idEvenement = " + evenementDTO.getIdEvenement();
                st.executeUpdate(SQL);
            }
            case "lieu" -> {
                evenementDTO.setLieu(valeur);
                String SQL = "UPDATE Evenement SET lieu = '" + valeur + "' WHERE idEvenement = " + evenementDTO.getIdEvenement();
                st.executeUpdate(SQL);
            }
            case "nombre" ->
            {
                evenementDTO.setNombrePersonneMax(Integer.parseInt(valeur));
                String SQL = "UPDATE Evenement SET nombrePersonneMax = " + Integer.parseInt(valeur) + " WHERE idEvenement = " + evenementDTO.getIdEvenement();
                st.executeUpdate(SQL);
            }
            case "nom" -> {
                evenementDTO.setNomEvenement(valeur);
                String SQL = "UPDATE Evenement SET nomEvenement = '" + valeur + "' WHERE idEvenement = " + evenementDTO.getIdEvenement();
                st.executeUpdate(SQL);
            }
        }
        return evenementDTO;
    }


    public static void retirerModerateurDuSalonSQL(SalonDTO salonDTO, UtilisateurDTO utilisateurDTO) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "DELETE FROM ModerateurSalon where idSalon="+salonDTO.getIdSalon()+" and idMembre="+utilisateurDTO.getIdUtilisateur();
        st.executeUpdate(SQL);
    }

    public static void ajouterModerateurAuSalon(UtilisateurDTO utilisateurDTO, SalonDTO salonDTO) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO ModerateurSalon (idSalon,idMembre) values ("+salonDTO.getIdSalon()+",+"+utilisateurDTO.getIdUtilisateur()+")";
        st.executeUpdate(SQL);
    }

    public static List<String> seDefiniCommePresentAUnEvenementSQL(UtilisateurDTO utilisateurDTO, EvenementDTO evenementDTO) throws SQLException {
        List<String> participants = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO PresenceEvenement (idMembre,idEvenement) values ("+utilisateurDTO.getIdUtilisateur()+",+"+evenementDTO.getIdEvenement()+")";
        st.executeUpdate(SQL);

        String SQL2 = "select * from PresenceEvenement where idEvenement="+evenementDTO.getIdEvenement();
        ResultSet rs = st.executeQuery(SQL2);
        while(rs.next()){
            participants.add(getUtilisateurByIdSQL(rs.getInt("idMembre")).getPseudo());
        }
        return participants;
    }



    public static List<String> seDefiniCommeAbsentAUnEvenementSQL(UtilisateurDTO utilisateurDTO, EvenementDTO evenementDTO) throws SQLException {
        List<String> participantsDTO = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "DELETE FROM PresenceEvenement WHERE idMembre = " + utilisateurDTO.getIdUtilisateur() + " AND idEvenement = " + evenementDTO.getIdEvenement();
        st.executeUpdate(SQL);

        String SQL2 = "select * from PresenceEvenement where idEvenement="+evenementDTO.getIdEvenement();
        ResultSet rs = st.executeQuery(SQL2);
        while(rs.next()){
            participantsDTO.add(getUtilisateurByIdSQL(rs.getInt("idMembre")).getPseudo());
        }
        return participantsDTO;
    }



    public static void validerEvenementSQL(EvenementDTO evenementDTO) throws SQLException {
        int idEvenement = evenementDTO.getIdEvenement();
        Statement st = connecterAuSalonSQL();
        String SQL = "UPDATE Evenement SET isValide = true WHERE idEvenement = '"+idEvenement+"';";
        st.executeUpdate(SQL);
    }


    //TODO TRY
    public static void envoyerMessageSalonSQL(SalonDTO salonDTO, MessageDTO messageDTO) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO MessageSalon (idSalon,nomAuteur,contenu,dateMessage) values ("+salonDTO.getIdSalon()+",'"+messageDTO.getAuteur()+"','"+messageDTO.getContenu()+"','"+messageDTO.getDate()+"')";
        st.executeUpdate(SQL);
    }

    //TODO TRY

    public static void envoyerMessageEventSQL(EvenementDTO evenementDTO, MessageDTO messageDTO) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "INSERT INTO MessageEvenement (idEvenement,nomAuteur,contenu,dateMessage) values ("+evenementDTO.getIdEvenement()+",'"+messageDTO.getAuteur()+"','"+messageDTO.getContenu()+"','"+messageDTO.getDate()+"')";
        st.executeUpdate(SQL);
    }


    //TODO TRY

    public static List<MessageDTO> getMessageSalonSQL(int numSalon) throws SQLException {
        ArrayList<MessageDTO> lesMessages = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQLMessage = "select * from MessageSalon where idSalon="+numSalon;
        ResultSet rs = st.executeQuery(SQLMessage);
        while(rs.next()){
            MessageDTO message = new MessageDTO();
            String auteur = rs.getString("nomAuteur");
            String contenu = rs.getString("contenu");
            String dateMessage = rs.getString("dateMessage");
            message.setAuteur(auteur);
            message.setContenu(contenu);
            message.setDate(dateMessage);
            lesMessages.add(message);
        }
        return lesMessages;
    }


    //TODO TRY
    public static List<MessageDTO> getMessageEventSQL(int idEvenement) throws SQLException {
        ArrayList<MessageDTO> lesMessages = new ArrayList<>();
        Statement st = connecterAuSalonSQL();
        String SQL = "select * from MessageEvenement where idEvenement="+idEvenement;
        ResultSet rs = st.executeQuery(SQL);
        while(rs.next()){
            MessageDTO message = new MessageDTO();
            String auteur = rs.getString("nomAuteur");
            String contenu = rs.getString("contenu");
            String dateMessage = rs.getString("dateMessage");
            message.setAuteur(auteur);
            message.setContenu(contenu);
            message.setDate(dateMessage);
            lesMessages.add(message);
        }
        return lesMessages;
    }

    //TODO TRY
    public static void supprimerUtilisateurSQL(Utilisateur utilisateur) throws SQLException {
        Statement st = connecterAuSalonSQL();
        String SQL = "DELETE FROM Membre where idMembre="+utilisateur.getIdUtilisateur();
        st.executeUpdate(SQL);
    }
}
