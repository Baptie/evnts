package bdd;

import java.sql.*;

import auth.dto.UtilisateurDTO;
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
        ResultSet rs = executeQueryForUser(ancienPseudo);
        if (rs.next()) {
            String SQL = "UPDATE Utilisateur SET pseudo = '" + nouveauPseudo + "' WHERE pseudo = '" + ancienPseudo + "'";
            st.executeUpdate(SQL);
        } else {
            throw new UtilisateurInexistantException();
        }
    }



    public void resetMDP(String pseudo, String nouveauMDP) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        pseudo = pseudo.replace("'", "\\'");
        ResultSet rs = executeQueryForUser(pseudo);
        if (rs.next()) {
            String SQL = "UPDATE Utilisateur SET motDePasse = '" + nouveauMDP + "' WHERE pseudo = '" + pseudo + "'";
            st.executeUpdate(SQL);
        } else {
            System.out.println("La combinaison pseudo et ancien mot de passe n'existe pas.");
        }
    }
    public void supprimerUtilisateur(String pseudo) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        ResultSet rs = executeQueryForUser(pseudo);
        if (rs.next()) {
            st.executeUpdate("DELETE FROM Utilisateur WHERE pseudo = '" + pseudo + "'");
        } else {
            System.out.println("L'utilisateur avec le pseudo " + pseudo + " n'existe pas.");
        }
    }

    public UtilisateurDTO connexionUtilisateur(String pseudo, String mdp) throws SQLException {
        System.out.println(pseudo + "    dfdsfsdfsdfdsfsdf " + mdp);
        Statement st = connecterAuthentificationSQL();
        UtilisateurDTO userDTO = new UtilisateurDTO();
        String SQLUser = "SELECT * FROM Utilisateur where pseudo='" + pseudo + "'AND motDePasse = '" + mdp + "'";
        ResultSet rs = st.executeQuery(SQLUser);
        while(rs.next()){
            int id = rs.getInt("idUtilisateur");
            String email = rs.getString("email");
            String pseudoSQL = rs.getString("pseudo");
            String motDePasse = rs.getString("motDePasse");

            System.out.println("email BDD : " + email);

            userDTO.setId(id);
            userDTO.setEmail(email);
            userDTO.setPseudo(pseudoSQL);
            userDTO.setMdp(motDePasse);
        }
        return userDTO;
    }

    private ResultSet executeQueryForUser(String pseudo) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String query = "SELECT * FROM Utilisateur WHERE pseudo = '" + pseudo + "'";
        return st.executeQuery(query);
    }
}
