package edu.cornell.opencomm.plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseService {

	public static final String DB_TABLE = "ofConference";
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
	/*Pull Conferences that the user is invited*/
	public String pullConferences(String username){
		try {
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.getResultSet();
			String result="";
			//if username="", pull all conferences;
			if(username ==""){
				rs=stmt.executeQuery("select distinct groupname from "+RF_TABLE);
				while (rs.next()){
						String gid=rs.getString("groupname");
						result += pullConference(gid);
				
				}
				Log.error("result in db: "+ result);
				return result;
			}
			//else: pull conferences for the specified username
			rs=stmt.executeQuery("select groupname from "+RF_TABLE+" where username= '"+ username+"'");
			
			while (rs.next()){
						String gid=rs.getString("groupname");
						result += pullConference(gid);
				
			}
			Log.error("result in db: "+ result);
			return result;
		}catch (SQLException e) {
				Log.error("Problem with database pulls. Returning null.", e);
		}
		return null;
		
	}
/*Pull information of the conference identified by the groupID; Conferences are separated by '$'*/
	public String pullConference(String groupID) {
		try {
			Connection dbConn = DbConnectionManager.getTransactionConnection();
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.getResultSet();
			
			Log.error("select * from " + DB_TABLE+ "where roomID= "+groupID);
			rs = stmt.executeQuery("select * from " + DB_TABLE+ " where roomID= "+groupID);
				
			
			
			String result = "";
			rs.next();
			
			roomID= rs.getString("roomID");
			roomname = rs.getString("roomname");
			invitername = rs.getString("invitername");
			description=rs.getString("description");
			    
			// yyyy-mm-dd hh:mm:ss
			starttime = rs.getString("starttime");
			endtime = rs.getString("endtime");
			recurrence = rs.getString("recurrence");
			result = "$"+roomID + "//" + roomname + "//" + invitername + "//" +starttime + "//" 
			+ endtime+"//"+recurrence+"//"+description;
			
			
			Statement stmt_plist = dbConn.createStatement();
			ResultSet rs_plist = stmt.getResultSet();
			rs_plist = stmt_plist.executeQuery("select username from " + RF_TABLE+" where groupname= "+groupID);
			result +="%";
			while (rs_plist.next()){
					
				result += "//"+rs_plist.getString("username");
					
			}
			
			result+= "";
			Log.error("result: "+result);
			return result;
		} catch (SQLException e) {
			Log.error("Problem with database pull. Returning null.", e);
		}
		return null;
	}
	
/*push the conference to the database*/
	public String push(Hashtable<String, String> pkt) {
		Connection dbConn=null;
		try {
			dbConn = DbConnectionManager.getTransactionConnection();
			dbConn.setAutoCommit(false);
			Statement stmt = dbConn.createStatement();
			Statement stmtRT=dbConn.createStatement();
			
			roomID= (String)pkt.get("roomID");
			Log.error("roomID: "+roomID);
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
				String sqlToRT="INSERT INTO "+RF_TABLE+"() VALUE ('"+roomID+"','"+participant+"', '0')";
				Log.error("sql1: "+sqlToRT);
				stmtRT.execute(sqlToRT);
				participant=(String)pkt.get("participant"+(++counter));
				}
			// insert the inviter
			participant=(String) pkt.get("invitername");
			
			stmtRT.execute("INSERT INTO "+RF_TABLE+"() VALUE ('"+roomID+"','"+participant+"', '1')");
			Log.error("inserting conference");
			// insert conference
			String sqlString="INSERT INTO "+DB_TABLE+"()VALUE ('"+roomID+"','"+roomname+"','"+invitername+"','"+starttime+"','"+endtime+"','"
			                  +recurrence+"','"+description+"')";
			Log.error("push sql: "+sqlString);
			stmt.executeUpdate(sqlString);
			
			dbConn.commit();

			//close all connections
			stmt.close();
			stmtRT.close();
			dbConn.close();
			
			
			return "SUCCESS";
		} catch (SQLException e) {
			Log.error("Problem with database push.", e);
			 
			 try {
				dbConn.rollback();
			} catch (SQLException error) {
				e.printStackTrace();
			}
     
		}
		return "FAIL";
	}
}
