package MySQLDemo;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDemo {
	static final String databasePrefix ="cs366-2207_salazarbc24";
	static final String netID ="salazarbc24"; // Please enter your netId
	static final String hostName ="washington.uww.edu"; //140.146.23.39 or washington.uww.edu
	static final String databaseURL ="jdbc:mariadb://" + hostName + "/" + databasePrefix;
	static final String password="bs0288"; // please enter your own password

	public static void main(String args[]) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			System.out.println("databaseURL: "+ databaseURL);
			
			//DriverManager.getConnection("jdbc:mariadb://192.168.100.174/db", "root", "root");
			connection = DriverManager.getConnection(databaseURL, netID, password);
			System.out.println("Successfully connected to the database");
		 }
		catch (ClassNotFoundException e) {
			System.out.println("Connection to the database is unsuccessful");
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			try {
				connection.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}