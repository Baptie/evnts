package gestSal.service;

import gestSal.dto.SalonDTO;
import gestSal.dto.UtilisateurDTO;
import gestSal.facade.FacadeSalon;
import gestSal.facade.FacadeSalonImpl;
import gestSal.facade.erreurs.NomSalonVideException;
import gestSal.facade.erreurs.NumeroSalonVideException;
import gestSal.facade.erreurs.SalonInexistantException;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;

import java.sql.*;

public class SalonSql {

    static FacadeSalon facadeSalon = new FacadeSalonImpl();

    public static void main(String[] args) throws SQLException {
        UtilisateurDTO utilisateurDTO = getUtilisateurByPseudoSQL("Camacho");
        SalonDTO salonDTO = getSalonById(1);
        System.out.println("pseudo "+utilisateurDTO.getPseudo());
        System.out.println("id "+ utilisateurDTO.getIdUtilisateur());
        rejoindreSalonSql(utilisateurDTO,salonDTO);
    }

    public SalonSql() {
    }

    public static Statement connecterAuSalonSQL() throws SQLException {
        // Connexion à la base de données MySQL
        String jdbcUrl = "jdbc:mysql://localhost:3307/salon";
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

    public static void rejoindreSalonSql(UtilisateurDTO utilisateurDTO, SalonDTO salonDTO) throws SQLException {
        int idUser = utilisateurDTO.getIdUtilisateur();
        int idSalon = salonDTO.getIdSalon();
        Statement st = connecterAuSalonSQL();
        String SQL = "insert into SalonMembre(idSalon, idMembre) VALUES ("+idSalon+", "+idUser+")";
        st.executeUpdate(SQL);

    }
}
