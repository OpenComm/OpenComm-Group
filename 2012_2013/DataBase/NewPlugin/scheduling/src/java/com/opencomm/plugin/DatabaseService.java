package java.com.opencomm.plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseService {

	public static final String DB_TABLE = "conference";
	public static final String RF_TABLE="ofGroupUser";// the referenced table
	

	private static final Logger Log = LoggerFactory
			.getLogger(DatabaseService.class);
	
	private String roomID;
	private String roomname;
	private String invitername;
	private String starttime;
	private String endtime;
	private String recurrence;
	private String description;
	
/*Pull information of the conference identified by the groupID*/
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
	

	public String push(Hashtable<String, String> pkt) {
		try {
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			Statement stmtRT=dbConn.createStatement();
			
			roomID= (String)pkt.get("roomID");
			roomname = (String)pkt.get("roomname");
			invitername = (String)pkt.get("invitername");
			    
			starttime = (String)pkt.get("starttime");
			endtime = (String)pkt.get("endtime");
			recurrence = (String)pkt.get("recurrence");
			description=(String)pkt.get("description");
			//Add Participants
			int counter=0;
			String participant=(String)pkt.get("participant"+counter);
			while(participant != null){
				String sqlToRT="INSERT INTO "+RF_TABLE+"() VALUE ("+roomID+","+participant+", 0)";
				stmtRT.execute(sqlToRT);
				participant=(String)pkt.get("participant"+(++counter));
				}
			// insert conference
			String sqlString="INSERT INTO "+DB_TABLE+"()VALUE ("+roomID+","+roomname+","+invitername+","+starttime+","+endtime+","
			                  +recurrence+","+description+")";
			stmt.executeUpdate(sqlString);
			
			
			
			return "SUCCESS";
		} catch (SQLException e) {
			Log.error("Problem with database push.", e);
		}
		return "FAIL";
	}
}
