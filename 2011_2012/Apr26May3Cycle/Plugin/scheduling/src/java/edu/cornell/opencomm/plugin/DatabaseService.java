package edu.cornell.opencomm.plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {

	public static final String DB_URL = "jdbc:mysql://localhost:3306/scheduling";
	public static final String DB_NAME = "scheduling";
	public static final String DB_TABLE = "conferences";
	public static final String DB_USER = "scheduler";
	public static final String DB_PASSWORD = "opencommsec";

	private static final Logger Log = LoggerFactory
			.getLogger(DatabaseService.class);
	
	private String id;
	private String owner;
	private String date;
	private String start;
	private String end;
	private String recurring;
	private String participant1;
	private String participant2;
	private String participant3;
	private String participant4;
	private String participant5;
	private String participant6;
	private String participant7;
	private String participant8;
	private String participant9;

	static Connection dbConn;

	public DatabaseService() {
		connect();
	}

	public void connect() {
		try {
			dbConn = DbConnectionManager.getTransactionConnection();
		} catch (SQLException e) {
			Log.error("SQLException: " + e.getMessage(), e);
			Log.error("SQLState: " + e.getSQLState(), e);
			Log.error("VendorError: " + e.getErrorCode(), e);
		}
	}

	public void closeConnection() {
		DbConnectionManager.closeTransactionConnection(dbConn, true);
	}

	public String pullConferences() {
		try {
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.getResultSet();
			rs = stmt.executeQuery("select * from " + 
					DB_NAME + "." + DB_TABLE);
			String result = "";
			while (rs.next()) {
				id = rs.getString("id");
				owner = rs.getString("OWNER");
				date = rs.getString("DATE");
				start = rs.getTimestamp("START").getTime() + "";
				end = rs.getTimestamp("END").getTime() + "";
				recurring = rs.getString("RECURRING");
				participant1 = rs.getString("PARTICIPANT1");
				participant2 = rs.getString("PARTICIPANT2");
				participant3 = rs.getString("PARTICIPANT3");
				participant4 = rs.getString("PARTICIPANT4");
				participant5 = rs.getString("PARTICIPANT5");
				participant6 = rs.getString("PARTICIPANT6");
				participant7 = rs.getString("PARTICIPANT7");
				participant8 = rs.getString("PARTICIPANT8");
				participant9 = rs.getString("PARTICIPANT9");
				result += id + "//" + owner + "//" + date + "//" + start + "//" 
						+ end + "//" + recurring + "//" + participant1 + 
						"//" + participant2 + "//"+ participant3 + "//"
						+ participant4 + "//"+ participant5 + "//"
						+ participant6 + "//"+ participant7 + "//"
						+ participant8 + "//"+ participant9 + "//" + "\n";
			}
			return result;
		} catch (SQLException e) {
			Log.error("Problem with database pull. Returning null.", e);
		}
		return null;
	}

	public boolean push(String sqlString) {
		try {
			Statement stmt = dbConn.createStatement();
			stmt.executeUpdate(sqlString);
			return true;
		} catch (SQLException e) {
			Log.error("Problem with database push.", e);
		}
		return false;
	}
}
