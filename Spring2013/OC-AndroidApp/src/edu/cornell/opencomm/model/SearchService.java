package edu.cornell.opencomm.model;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.search.UserSearchManager;

import edu.cornell.opencomm.network.NetworkService;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
* @author Antoine Chkaiban
* Class to implement search services:
* both to be able to search for users and to retrieve JID with email in order to reset password
*/
public abstract class SearchService {

   private static final String TAG = SearchService.class.getSimpleName();

   /**
    * For reseting password purposes. For search purposes use searchByEmail method.
    * @param String - the email address of the user we are looking for
    * @return String - the jid of the user associated with that email (if it exists) - string "0" otherwise.
    */
   public static String getJid(String email) {
	  String jid = "0";
      try {
         NetworkService.getInstance().getConnection().loginAnonymously();
         UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
         
         //testing purposes
         Collection<?> services = search.getSearchServices();
         Iterator<?> it = services.iterator();
         while(it.hasNext()){
        	 Log.v(TAG, it.next().toString());
         }
         
         //Create search form
         Form searchForm = search.getSearchForm("search." + "opencomm");
         
         //testing purposes
         Log.v(TAG, "Available search fields:");
         Iterator<FormField> fields = searchForm.getFields();
         while (fields.hasNext()) {
            FormField field = fields.next();
            Log.v(TAG, field.getVariable() + " : " + field.getType());
         }
         
         //create answer form
         Form answerForm = searchForm.createAnswerForm();
         answerForm.setAnswer("search", email);
         answerForm.setAnswer("Email", true);
         
         //get search results
         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
         
         //Close the connection
         NetworkService.getInstance().getConnection().disconnect();
         
         //Process the data
         
         //testing purposes
         Log.v(TAG, "\nColumns that are included as part of the search results:");
         Iterator<Column> columns = data.getColumns();
         while (columns.hasNext()) {
            Log.v(TAG, columns.next().getVariable());
         }
         
         //Hits
         Log.v(TAG, "\nThe jids and emails from our each of our hits:");
         Iterator<Row> rows = data.getRows();
         if (rows.hasNext()) {
            Row row = rows.next();
            
            @SuppressWarnings("unchecked")
			Iterator<String> jids = row.getValues("jid");
            @SuppressWarnings("unchecked")
			Iterator<String> emails = row.getValues("email");
            String jidFound = null;
            String emailFound = null;
            
            if (emails.hasNext() && jids.hasNext()) {
               jidFound = jids.next();
               emailFound = emails.next();
               Log.v(TAG, jidFound);
               Log.v(TAG, emailFound);
               if(emailFound.equalsIgnoreCase(email)){
            	   String[] jidCleaned = jidFound.split("@");
            	   jid = jidCleaned[0];
            	   Log.v(TAG, jid);
               } 
            } 
         }
         return jid;
      } catch (Exception ex) {
         Log.v(TAG, "Caught Exception :"+ex.getMessage());
         return jid;
      }
   }
   /**
    * @param String - the email of the user we are looking for
    * @return boolean - true if the user exists in the database - else otherwise
    */
   public static boolean emailAlreadyExists(String email) {
	   try {
	    	 Log.v(TAG, "emailAlreadyExists method executed");
	    	 //Login anonymously and create search manager
	    	 NetworkService.getInstance().getConnection().loginAnonymously();
	         UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
	         //Create search form
	         Form searchForm = search.getSearchForm("search." + "opencomm");
	         //create answer form
	         Form answerForm = searchForm.createAnswerForm();
	         answerForm.setAnswer("search", email);
	         answerForm.setAnswer("Email", true);
	         //get search results
	         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
	         //Close the connection
	         NetworkService.getInstance().getConnection().disconnect();
	         //Hits:
	         Iterator<Row> rows = data.getRows();
	         if (rows.hasNext()) {
	            Row row = rows.next();
	            @SuppressWarnings("unchecked")
				Iterator<String> emails = row.getValues("email");
	            
	            String emailFound = null; 
	            if (emails.hasNext()) {
	               emailFound = emails.next();
	               if(emailFound.equalsIgnoreCase(email)){
	            	  Log.v(TAG, "Found existing email: " + emailFound);
	            	  return true;
	               } 
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return false;
   }
   
   
   /**
    * @param String - the jid of the user we are looking for
    * @return boolean - true if the user exists in the database - else otherwise
    */
   public static boolean jidAlreadyExists(String jid) {
	   try {
	    	 Log.v(TAG, "jidAlreadyExists method executed");
	    	 //Login anonymously and create search manager
	    	 NetworkService.getInstance().getConnection().loginAnonymously();
	         UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
	         //Create search form
	         Form searchForm = search.getSearchForm("search." + "opencomm");
	         //create answer form
	         Form answerForm = searchForm.createAnswerForm();
	         answerForm.setAnswer("search", jid);
	         answerForm.setAnswer("Username", true);
	         //get search results
	         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
	         //Close the connection
	         NetworkService.getInstance().getConnection().disconnect();
	         //Hits:
	         Iterator<Row> rows = data.getRows();
	         if (rows.hasNext()) {
	            Row row = rows.next();
	            @SuppressWarnings("unchecked")
				Iterator<String> jids = row.getValues("jid");
	            String jidFound = null;
	            
	            if (jids.hasNext()) {
	               jidFound = jids.next();
	               if(jidFound.equalsIgnoreCase(jid)){
	            	   Log.v(TAG, "Found existing Username: " + jidFound);
	            	   return true;
	               } 
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return false;
   }
   /**
    * @param String - the jid of the user we are looking for
    * @return String [] - A list of jids of users that match that jid
    * Only call this method when authenticated.
    */
   public static ArrayList<User> searchByJid(String jid) {
	   ArrayList<User> results = new ArrayList<User>();
	   try {
	    	 Log.v(TAG, "searchByJid method executed");
	    	 //Create search manager
	         UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
	         //Create search form
	         Form searchForm = search.getSearchForm("search." + "opencomm");
	         //create answer form
	         Form answerForm = searchForm.createAnswerForm();
	         answerForm.setAnswer("search", jid);
	         answerForm.setAnswer("Username", true);
	         //get search results
	         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
	         //get roster (to check if user is already in buddylist, do not include in search results)
	         Roster roster = NetworkService.getInstance().getConnection().getRoster();
	         //Hits:
	         Iterator<Row> rows = data.getRows();
	         while (rows.hasNext()) {
	            Row row = rows.next();
	            @SuppressWarnings("unchecked")
				Iterator<String> jids = row.getValues("jid");
	            while (jids.hasNext()) {
	            	String jidFound = jids.next();
	            	if (! roster.contains(jidFound)) {
	            		VCard vCard = new VCard();
		            	vCard.load(NetworkService.getInstance().getConnection(), jidFound);
		            	results.add(new User(vCard));
	            	}
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return results;
   }
   
   /**
    * @param String - the email of the user we are looking for
    * @return String [] - A list of jids of users that match that email
    * Only call this method when authenticated.
    */
   public static ArrayList<User> searchByEmail(String email) {
	   ArrayList<User> results = new ArrayList<User>();
	   try {
	    	 Log.v(TAG, "searchByEmail method executed");
	    	 //Create search manager
	         UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
	         //Create search form
	         Form searchForm = search.getSearchForm("search." + "opencomm");
	         //create answer form
	         Form answerForm = searchForm.createAnswerForm();
	         answerForm.setAnswer("search", email);
	         answerForm.setAnswer("Email", true);
	         //get search results
	         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
	         //get roster (to check if user is already in buddylist, do not include in search results)
	         Roster roster = NetworkService.getInstance().getConnection().getRoster();
	         //Hits:
	         Iterator<Row> rows = data.getRows();
	         while (rows.hasNext()) {
	            Row row = rows.next();
	            @SuppressWarnings("unchecked")
				Iterator<String> jids = row.getValues("jid");
	            while (jids.hasNext()) {
	            	String jidFound = jids.next();
	            	if (! roster.contains(jidFound)) {
	            		VCard vCard = new VCard();
		            	vCard.load(NetworkService.getInstance().getConnection(), jidFound);
		            	results.add(new User(vCard));
	            	}
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return results;
   }
   
   /**
    * @param String - the name of the user we are looking for
    * @return String [] - A list of jids of users that match that name
    * Only call this method when authenticated.
    */
   public static ArrayList<User> searchByName(String name) {
	   ArrayList<User> results = new ArrayList<User>();
	   try {
	    	 Log.v(TAG, "searchByName method executed");
	    	 //Create search manager
	         UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
	         //Create search form
	         Form searchForm = search.getSearchForm("search." + "opencomm");
	         //create answer form
	         Form answerForm = searchForm.createAnswerForm();
	         answerForm.setAnswer("search", name);
	         answerForm.setAnswer("Name", true);
	         //get search results
	         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
	         //get roster (to check if user is already in buddylist, do not include in search results)
	         Roster roster = NetworkService.getInstance().getConnection().getRoster();
	         //Hits:
	         Iterator<Row> rows = data.getRows();
	         while (rows.hasNext()) {
	            Row row = rows.next();
	            @SuppressWarnings("unchecked")
				Iterator<String> jids = row.getValues("jid");
	            while (jids.hasNext()) {
	            	String jidFound = jids.next();
	            	if (! roster.contains(jidFound)) {
	            		VCard vCard = new VCard();
		            	vCard.load(NetworkService.getInstance().getConnection(), jidFound);
		            	results.add(new User(vCard));
	            	}
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return results;
   }
}