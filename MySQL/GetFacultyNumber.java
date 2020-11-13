import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Types;

public class GetFacultyNumber {
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

	public void storeProcedure(String spName) {
		try {
			statement = connection.createStatement();
			int total = 0;
			CallableStatement myCallStatement = connection.prepareCall("{call " 
												+ spName + "(?)}");
			myCallStatement.registerOutParameter(1, Types.BIGINT);
			myCallStatement.execute();
			total = myCallStatement.getInt(1);
			System.out.println("Total Total Faculty = " + total);
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public static void main(String args[]) {
		GetFacultyNumber procedureOBJ = new GetFacultyNumber();
		procedureOBJ.Connection();
		String spName = "getFacultyNumber";
		procedureOBJ.storeProcedure(spName);
	}
}

/**
delimiter $$
drop procedure if exists getFacultyNumber;
create procedure getFacultyNumber(INOUT total int) 
begin 
	  select count(*) into total
	  from Faculty;
end $$
delimiter;
*/