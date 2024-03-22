package gestSal.service;

import gestSal.dto.SalonDTO;
import gestSal.modele.Salon;

import java.sql.*;

public class SalonSql {


    public static void main(String[] args) throws SQLException {
        getSalonById(2);
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
        System.out.println(id);
        System.out.println(salon.getNomSalon());
        System.out.println(salon.getNomCreateur());
        System.out.println(salon.getLogo());
        return salon;

    }

}
