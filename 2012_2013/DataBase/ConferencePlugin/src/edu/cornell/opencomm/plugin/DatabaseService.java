package edu.cornell.opencomm.plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jivesoftware.smack.packet.Packet;

public class DatabaseService {

	public static final String DB_TABLE = "CONFERENCES";
	public static final String RF_TABLE="ofGroupUser";
	

	private static final Logger Log = LoggerFactory
			.getLogger(DatabaseService.class);
	/*omID int primary key not null, roomname varchar(25), invitername varchar(25), starttime datetime, endtime datetime, recurrence varchar(25), description varchar(2000)*/
	private String roomID;
	private String roomname;
	private String invitername;
	//private String date;
	private String date;
	private String starttime;
	private String endtime;
	private String recurrence;
	private String description;
	
//To pull one conference with the specific conference ID
	public String pullConference(String groupID) {
		try {
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.getResultSet();
			rs = stmt.executeQuery("select * from " + DB_TABLE+ "where roomID= "+groupID);
			Statement stmt_plist = dbConn.createStatement();
			ResultSet rs_plist = stmt.getResultSet();
			rs_plist = stmt_plist.executeQuery("select username from " + RF_TABLE+"where groupname= "+groupID);
			String result = "";
			rs.next();
			roomID= rs.getString("roomID");
			roomname = rs.getString("roomname");
			invitername = rs.getString("invitername");
			description=rs.getString("description");
			    
			// timestamp in  JDBC timestamp escape format:
			// yyyy-mm-dd hh::mm::ss
			starttime = rs.getTimestamp("starttime").toString() + "";
			endtime = rs.getTimestamp("endtime").toString() + "";
			recurrence = rs.getString("recurrence");
			result = roomname + "//" + roomID + "//" + invitername + "//" +starttime + "//" 
			+ endtime+"//"+recurrence+"//"+description;
			
			while (rs_plist.next()){
				
				result += "//"+rs_plist.getString("username");
				
			}
				result+= "\n";
			
			return result;
		} catch (SQLException e) {
			Log.error("Problem with database pull. Returning null.", e);
		}
		return null;
	}
	

	public boolean push(Packet pkt) {
		try {
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			Statement stmtRT=dbConn.createStatement();
			
			roomID= (String)pkt.getProperty("roomID");
			roomname = (String)pkt.getProperty("roomname");
			invitername = (String)pkt.getProperty("invitername");
			    
			// timestamp in  JDBC timestamp escape format:
			// yyyy-mm-dd hh::mm::ss
			starttime = (String)pkt.getProperty("starttime");
			endtime = (String)pkt.getProperty("endtime");
			recurrence = (String)pkt.getProperty("recurrence");
			
			String sqlString="INSERT INTO "+DB_TABLE+"()VALUE ("+roomID+","+roomname+","+invitername+","+starttime+","+endtime+","
			                  +recurrence+","+description+")";
			stmt.executeUpdate(sqlString);
			int counter=1;
			String participant=(String)pkt.getProperty("PARTICIPANT"+counter);
			while(participant != null){
			String sqlToRT="INSERT INTO "+RF_TABLE+"() VALUE ("+roomID+","+participant+", 0, )";
			stmtRT.execute(sqlToRT);
			participant="PARTICIPANT"+(++counter);
			}
			
			return true;
		} catch (SQLException e) {
			Log.error("Problem with database push.", e);
		}
		return false;
	}
}
