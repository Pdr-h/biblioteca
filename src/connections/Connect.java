package connections;

import java.sql.*;

public class Connect {
    //private static final String url = "jdbc:mysql://monorail.proxy.rlwy.net:26158/railway";
    //private static final String user = "root";
    //private static final String password = "fqghDHgiBrmAkwSkWVQcyCmxfvTVbLLp";

    private static final String url = "jdbc:mysql://127.0.0.1:3306/library";
    private static final String user = "root";
    private static final String password = "0712peu";
    private static Connection conn;

    //Criando uma conexao com o banco usando a bilbioteca sql
    public static Connection getConnect() {
        try {

            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, user, password);
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    }
