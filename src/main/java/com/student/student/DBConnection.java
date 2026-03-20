import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://hopper.proxy.rlwy.net:30375/railway?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "fNOhtFhxDVQDINvjMuyDRJHmynyNAkvl"
            );

            System.out.println("Database Connected Successfully");

        } catch (Exception e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        }

        return con;
    }
}