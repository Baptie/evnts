package gestSal.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Fonctions {
    public static String url = "jdbc:mysql://mysql-rpplf.alwaysdata.net:3306/rpplf_evnt?useSSL=true&enabledTLSProtocols=TLSv1.2";
    public static final String user = "rpplf_evnt";
    public static final String mdp = "MonSuperMotDePasse!";

    public static Statement connexionSQLBDD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, mdp);
            Statement st = conn.createStatement();
            return st;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}