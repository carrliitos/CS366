import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
	static final String databasePrefix = "cs366-2207_salazarbc24";
	static final String netID = "salazarbc24";
	static final String hostName = "washington.uww.edu"; //140.146.23.39 or washington.uww.edu
	static final String databaseURL = "jdbc:mariadb://" + hostName + "/" + databasePrefix;
	static final String password = "bs0288";

	public static void main(String args[]) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			System.out.println("Database URL: " + databaseURL);
			connection = DriverManager.getConnection(databaseURL, netID, password);
			System.out.println("SUCCESSFULL CONNECTED TO THE DATABASE!");
		}
		catch(ClassNotFoundException e) {
			System.out.println("!! UNSUCCESSFULL CONNECTION TO DATABASE !!");
			e.printStackTrace();
		}
		catch(SQLException e) {
			System.out.println("!! SQL EXCEPTION ERROR !!");
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			}
			catch(SQLException e) {
				System.out.println("!! SQL EXCEPTION ERROR !!");
				e.printStackTrace();
			}
		}
	}
}