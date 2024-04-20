package bdd;

import java.sql.*;

import auth.exception.EMailDejaPrisException;
import auth.exception.EmailOuPseudoDejaPrisException;
import auth.exception.UtilisateurInexistantException;


public class InteractionBDDAuthentification {




    public static Statement connecterAuthentificationSQL() throws SQLException {
        String jdbcUrl = "jdbc:mysql://dbAuthentification:3306/authentification";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        Statement statement = connection.createStatement();
        return statement;
    }

    public void enregistrerUtilisateur(String email, String pseudo, String mdp) throws SQLException, EMailDejaPrisException, EmailOuPseudoDejaPrisException {
        Statement st = connecterAuthentificationSQL();
        ResultSet rs = st.executeQuery("SELECT * FROM Utilisateur WHERE email = '" + email + "' OR pseudo = '" + pseudo + "'");
        if (rs.next()) {
            throw new EmailOuPseudoDejaPrisException();
        } else {
            email = email.replace("'", "\\'");
            pseudo = pseudo.replace("'", "\\'");
            String SQL = "INSERT INTO Utilisateur(email, pseudo, motDePasse) VALUES ('" + email + "', '" + pseudo + "','" + mdp + "')";
            st.executeUpdate(SQL);
        }
    }



    public void resetPseudo(String ancienPseudo, String nouveauPseudo) throws SQLException, UtilisateurInexistantException {
        Statement st = connecterAuthentificationSQL();
        ResultSet rs = st.executeQuery("SELECT * FROM Utilisateur WHERE pseudo = '" + ancienPseudo + "'");
        if (rs.next()) {
            String SQL = "UPDATE Utilisateur SET pseudo = '" + nouveauPseudo + "' WHERE pseudo = '" + ancienPseudo + "'";
            st.executeUpdate(SQL);
        } else {
            throw new UtilisateurInexistantException();
        }
    }



    public void resetMDP(String pseudo, String nouveauMDP) throws SQLException, UtilisateurInexistantException {
        Statement st = connecterAuthentificationSQL();
        pseudo = pseudo.replace("'", "\\'");
        ResultSet rs = st.executeQuery("SELECT * FROM Utilisateur WHERE pseudo = '" + pseudo + "'");
        if (rs.next()) {
            String SQL = "UPDATE Utilisateur SET motDePasse = '" + nouveauMDP + "' WHERE pseudo = '" + pseudo + "'";
            st.executeUpdate(SQL);
        } else throw new UtilisateurInexistantException();

    }
    public void supprimerUtilisateur(String pseudo) throws SQLException, UtilisateurInexistantException {
        Statement st = connecterAuthentificationSQL();
        ResultSet rs = st.executeQuery("SELECT * FROM Utilisateur WHERE pseudo = '" + pseudo + "'");
        if (rs.next()) {
            st.executeUpdate("DELETE FROM Utilisateur WHERE pseudo = '" + pseudo + "'");
        } else throw new UtilisateurInexistantException();
    }

}
