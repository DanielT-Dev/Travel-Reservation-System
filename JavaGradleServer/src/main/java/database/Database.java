package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static String URL;

    static {
        try {
            Class.forName("org.sqlite.JDBC");

            Properties props = new Properties();
            props.load(Database.class.getClassLoader().getResourceAsStream("db.config"));
            URL = props.getProperty("jdbc.url");

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
