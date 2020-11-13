import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Types;

public class CursorClasses {
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

	public void myCursor(String spName) {
		try {
			statement = connection.createStatement();
			String listName;
			CallableStatement myCallStmt = connection.
					prepareCall("{call "+spName+"(?)}");
			myCallStmt.setString(1, "");
			myCallStmt.registerOutParameter(1, Types.VARCHAR);
			myCallStmt.execute();
			listName = myCallStmt.getString(1);
			System.out.println("Class Information: \n" + listName);
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public static void main(String args[]) {
		CursorClasses demoObj = new CursorClasses();
		demoObj.Connection();
		String spName ="getClassInfo21";
		demoObj.myCursor(spName);
	}
}

/**
delimiter $$
drop procedure if exists getClassInfo21;
create procedure getClassInfo21(INOUT classList VARCHAR(4000))
begin
declare isDone default 0;
declare currentClass VARCHAR(255) default “”;
declare currentMeet VARCHAR(255) default “”;
declare currentRoom VARCHAR(255) default “”;
declare classCursor cursor for
select cname, meets_at, room from Class;
declare continue handler
for not found set isDone = 1;
open classCursor
getList: loop
fetch classCursor into currentClass, currentMeet, currentRoom;
if isDone = 1 then
leave getList;
end if;
set classList = concat("","\n", classList);
set classList = concat(currentRoom,"\t", classList);
set classList = concat(currentMeet,"\t", classList);
set classList = concat(currentClass,"\t\t", classList);
end loop getList;
close classCursor;
end $$
delimiter;
*/