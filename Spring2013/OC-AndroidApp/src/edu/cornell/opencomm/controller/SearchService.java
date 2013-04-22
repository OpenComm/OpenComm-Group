package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import edu.cornell.opencomm.network.NetworkService;

import android.util.Log;

import java.util.Collection;
import java.util.Iterator;

/**
* @author Antoine Chkaiban
* Class to implement search services:
* both to be able to search for users and to retrieve JID with email in order to reset password
*/
public abstract class SearchService {
   private static String DOMAIN = "cuopencomm.no-ip.org";
   private static int PORT = 5222;
   private static final String TAG = SearchService.class.getSimpleName();

   
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
            
            Iterator<String> jids = row.getValues("jid");
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
	         Log.v(TAG, "\nThe emails from our each of our hits:");
	         Iterator<Row> rows = data.getRows();
	         while (rows.hasNext()) {
	            Row row = rows.next();
	            
	            Iterator<String> emails = row.getValues("email");
	            String emailFound = null;
	            
	            if (emails.hasNext()) {
	               emailFound = emails.next();
	               Log.v(TAG, emailFound);
	               if(emailFound.equalsIgnoreCase(email)){
	            	  return true;
	               } 
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return false;
   }
   
   public static boolean jidAlreadyExists(String jid) {
	   try {
	    	 Log.v(TAG, "jidAlreadyExists method executed");
	    	 
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
	         answerForm.setAnswer("search", jid);
	         answerForm.setAnswer("Username", true);
	         
	         //get search results
	         ReportedData data = search.getSearchResults(answerForm, "search." + "opencomm");
	         
	         //Close the connection
	         NetworkService.getInstance().getConnection().disconnect();
	         
	         Log.v(TAG, "\nColumns that are included as part of the search results:");
	         Iterator<Column> columns = data.getColumns();
	         while (columns.hasNext()) {
	            Log.v(TAG, columns.next().getVariable());
	         }
	         
	         Log.v(TAG, "\nThe jids from our each of our hits:");
	         Iterator<Row> rows = data.getRows();
	         while (rows.hasNext()) {
	            Row row = rows.next();
	            
	            Iterator<String> jids = row.getValues("jid");
	            String jidFound = null;
	            
	            while (jids.hasNext()) {
	               jidFound = jids.next();
	               Log.v(TAG, jidFound);
	               if(jidFound.equalsIgnoreCase(jid)){
	            	   return true;
	               } 
	            } 
	         }
	   } catch (Exception ex) {
	         Log.v(TAG, "Caught Exception :"+ex.getMessage());
	   }
	   return false;
   }
}