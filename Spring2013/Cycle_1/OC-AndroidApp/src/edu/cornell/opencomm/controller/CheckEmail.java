package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.search.UserSearchManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CheckEmail {
   private static String DOMAIN = "cuopencomm.no-ip.org";
   private static int PORT = 5222;
   
   public static void main() {
      try {
    	 System.out.println("We're there");
    	 ConnectionConfiguration config = new ConnectionConfiguration(DOMAIN,PORT);
    	 config.setSASLAuthenticationEnabled(true);
         XMPPConnection con = new XMPPConnection(config);
         con.connect();
         con.login("oc1testorg","opencomm2012");
         System.out.println("Logged in");
         UserSearchManager search = new UserSearchManager(con);
         System.out.println("User Search created");
         Collection services = search.getSearchServices();
         System.out.println("Search Services found");
         Form searchForm = search.getSearchForm("search." + DOMAIN);
         System.out.println("Available search fields:");
         Iterator<FormField> fields = searchForm.getFields();
         while (fields.hasNext()) {
            FormField field = fields.next();
            System.out.println(field.getVariable() + " : " + field.getType());
         }
         
         Form answerForm = searchForm.createAnswerForm();
         answerForm.setAnswer("search", "oc2testorg@cuopencomm");
         answerForm.setAnswer("Email", true);
         
         ReportedData data = search.getSearchResults(answerForm, "search." + DOMAIN);
         
         System.out.println("\nColumns that are included as part of the search results:");
         Iterator<Column> columns = data.getColumns();
         while (columns.hasNext()) {
            System.out.println(columns.next().getVariable());
         }
         
         System.out.println("\nThe jids from our each of our hits:");
         Iterator<Row> rows = data.getRows();
         while (rows.hasNext()) {
            Row row = rows.next();
            
            Iterator<String> jids = row.getValues("jid");
            while (jids.hasNext()) {
               System.out.println(jids.next());
            }
         }
      } catch (Exception ex) {
         System.out.println("Caught Exception :"+ex.getMessage());
      }
   }
}