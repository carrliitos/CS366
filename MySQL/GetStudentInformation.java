import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GetStudentInformation {
	static final String databasePrefix = "cs366-2207_salazarbc24";
	static final String netID = "salazarbc24";
	static final String hostName = "washington.uww.edu"; //140.146.23.39 or washington.uww.edu
	static final String databaseURL = "jdbc:mariadb://" + hostName + "/" 
								+ databasePrefix;
	static final String password = "bs0288";

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	public void Connection() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			System.out.println("Database URL: " + databaseURL);
			connection = DriverManager.getConnection(databaseURL, netID, password);
			System.out.println("SUCCESSFULL CONNECTION TO THE DATABASE!");
		}
		catch(ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: " + e.getMessage());
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public void myQuery(String sqlQuery) {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columns = metaData.getColumnCount();
			for(int i = 1; i <= columns; i++) {
				System.out.print(metaData.getColumnName(i) + "\t");
			}
			System.out.println();
			while(resultSet.next()) {
				for(int i = 1; i <= columns; i++) {
					System.out.print(resultSet.getObject(i) + "\t\t");
				}
				System.out.println();
			}
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public static void main(String args[]) {
		GetStudentInformation procedureOBJ = new GetStudentInformation();
		procedureOBJ.Connection();
		try {
			Scanner userInput = new Scanner(System.in);
			System.out.println("\nEnter Student Number: ");
			String studentID = userInput.nextLine();

			String sqlQuery = "select * from Student where snum = '" + studentID + "';";
			procedureOBJ.myQuery(sqlQuery);
		}
		catch(Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
}

/**
delimiter $$
drop procedure if exists getStudentInformation;
create procedure getStudentInformation(IN studentID INT) 
begin 
	  select *
	  from Student
	  where snum = studentID;
end $$
delimiter;
*/