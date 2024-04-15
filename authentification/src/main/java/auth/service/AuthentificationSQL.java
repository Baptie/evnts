package auth.service;

import java.sql.*;

public class AuthentificationSQL {


    public static Statement connecterAuthentificationSQL() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/authentification";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        Statement statement = connection.createStatement();
        return statement;

    }

    public static void inscriptionSQL(String pseudo, String mdp, String eMail) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "insert into Utilisateur(email, pseudo, motDePasse) VALUES ('"+eMail+"', '"+pseudo+"','"+mdp+"')";
        st.executeUpdate(SQL);
    }

    public static void renameSQL(String pseudo, String nouveauPseudo) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "UPDATE Utilisateur SET pseudo = '"+nouveauPseudo+"' WHERE pseudo = '"+pseudo+"'";
        st.executeUpdate(SQL);
    }

    public static void resetMDP(String pseudo, String nouveauMDP) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "UPDATE Utilisateur SET motDePasse = '"+nouveauMDP+"' WHERE pseudo = '"+pseudo+"'";
        st.executeUpdate(SQL);
    }

    public static void supprimerUtilisateurSQL(String pseudo) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "DELETE FROM Utilisateur WHERE pseudo = '"+pseudo+"'";
        st.executeUpdate(SQL);

    }
}
