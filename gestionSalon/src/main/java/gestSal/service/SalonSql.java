package gestSal.service;

import java.sql.*;

public class SalonSql {



    public static void main(String[] args) {
        // Connexion à la base de données MySQL
        String jdbcUrl = "jdbc:mysql://localhost:3308/utilisateur";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
             Statement statement = connection.createStatement()) {

            // Exécution de la requête SQL
            ResultSet resultSet = statement.executeQuery("SELECT * FROM toto");

            // Traitement des résultats
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}
