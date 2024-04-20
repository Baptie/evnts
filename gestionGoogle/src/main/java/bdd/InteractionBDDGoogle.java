package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InteractionBDDGoogle {

    public InteractionBDDGoogle() {
    }

    public static Statement connecterAuthentificationSQL() throws SQLException {
        String jdbcUrl = "jdbc:mysql://dbAuthentification:3306/google";
        String jdbcUser = "root";
        String jdbcPassword = "root";

        Connection connection = DriverManager.getConnection(jdbcUrl,jdbcUser,jdbcPassword);
        Statement statement = connection.createStatement();
        return statement;
    }

    public void enregistrerUser(String email) throws SQLException {
        Statement st = connecterAuthentificationSQL();
        String SQL = "insert into Utilisateur(email) VALUES ('"+email+"')";
        st.executeUpdate(SQL);
    }
}
