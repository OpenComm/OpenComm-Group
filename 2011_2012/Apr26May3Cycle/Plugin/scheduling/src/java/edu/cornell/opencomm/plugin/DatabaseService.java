package edu.cornell.opencomm.plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {

	public static final String DB_TABLE = "CONFERENCES";

	private static final Logger Log = LoggerFactory
			.getLogger(DatabaseService.class);
	
	private String id;
	private String name;
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

	public String pullConferences() {
		try {
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.getResultSet();
			rs = stmt.executeQuery("select * from " + DB_TABLE);
			String result = "";
			while (rs.next()) {
				id = rs.getString("id");
				name = rs.getString("NAME");
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
				result += name + "//" + id + "//" + owner + "//" + date + "//" + start + "//" 
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
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			stmt.executeUpdate(sqlString);
			return true;
		} catch (SQLException e) {
			Log.error("Problem with database push.", e);
		}
		return false;
	}
}
