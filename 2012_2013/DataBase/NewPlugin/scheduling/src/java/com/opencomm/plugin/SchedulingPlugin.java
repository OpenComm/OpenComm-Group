package java.com.opencomm.plugin;


import java.io.File;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import org.xmpp.packet.Packet;
//import javax.xml.parsers.*;
import org.xml.sax.InputSource;
//import org.w3c.dom.*;
//import java.io.*;

/**
 * A plugin to allow conference scheduling with a MySQL database
 * 
 * @author Kris Kooi, Crystal Qin
 * */
public class SchedulingPlugin implements Plugin, Component,
		PropertyEventListener {

	private static final Logger Log = LoggerFactory
			.getLogger(SchedulingPlugin.class);

	private String serviceName;
	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private DatabaseService databaseService;

	public SchedulingPlugin() {
		serviceName = JiveGlobals.getProperty("plugin.scheduling.serviceName",
				"scheduling");
	}

	// Plugin interface
	@Override
	public void initializePlugin(PluginManager arg0, File arg1) {
		pluginManager = arg0;
		databaseService = new DatabaseService();

		// Register as a component
		componentManager = ComponentManagerFactory.getComponentManager();
		try {
			componentManager.addComponent(serviceName, this);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		PropertyEventDispatcher.addListener(this);
	}

	@Override
	public void destroyPlugin() {
		PropertyEventDispatcher.removeListener(this);
		// Unregister component
		if (componentManager != null) {
			try {
				componentManager.removeComponent(serviceName);
			} catch (Exception e) {
				Log.error(e.getMessage(), e);
			}
		}
		componentManager = null;
		pluginManager = null;
		databaseService = null;
	}

	// Component Interface
	@Override
	public void initialize(JID arg0, ComponentManager arg1)
			throws ComponentException {
	}

	@Override
	public void start() {
	}

	@Override
	public void shutdown() {
	}

	@Override
	public String getName() {
		// Get the name from the plugin.xml file
		return pluginManager.getName(this);
	}

	@Override
	public String getDescription() {
		// Get the name from the plugin.xml file
		return pluginManager.getDescription(this);
	}
	public Hashtable<String,String> parseXML(String xml){
		Hashtable<String, String> pktInfo=new Hashtable<String, String>();
		 try {
		        DocumentBuilderFactory dbf =
		            DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(xml));
		        Document doc = db.parse(is);
		       // Element docEle = doc.getDocumentElement();
		        NodeList roomID = doc.getElementsByTagName("roomID");
		        NodeList roomName = doc.getElementsByTagName("roomname");
		        NodeList invitername=doc.getElementsByTagName("invitername");
		        NodeList description = doc.getElementsByTagName("description");
		        NodeList starttime = doc.getElementsByTagName("starttime");
		        NodeList endtime = doc.getElementsByTagName("endtime");
		        NodeList recurrence = doc.getElementsByTagName("recurrence");
		        NodeList participants=doc.getElementsByTagName("participant");

		        String roomIDstr=roomID.item(0).getFirstChild().getNodeValue();
		        String roomNamestr=roomName.item(0).getFirstChild().getNodeValue();
		        String inviternamestr=invitername.item(0).getFirstChild().getNodeValue();
		        String descriptionstr=description.item(0).getFirstChild().getNodeValue();
		        String starttimestr=starttime.item(0).getFirstChild().getNodeValue();
		        String endtimestr=endtime.item(0).getFirstChild().getNodeValue();
		        String recurrencestr=recurrence.item(0).getFirstChild().getNodeValue();
		        
		        pktInfo.put("roomID", roomIDstr);
		        pktInfo.put("roomname", roomNamestr);
		        pktInfo.put("invitername", inviternamestr);
		        pktInfo.put("description", descriptionstr);
		        pktInfo.put("starttime",starttimestr );
		        pktInfo.put("endtime", endtimestr);
		        pktInfo.put("recurrence", recurrencestr);
		        
		        // iterate the participants
		        for (int i = 0; i < participants.getLength(); i++) {
		           Element person = (Element) participants.item(i);
		           pktInfo.put("participant"+i, person.getFirstChild().getNodeValue());

		        }
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		    }
		return pktInfo;
		
	}
	@Override
	public void processPacket(Packet arg0) {
		Message reply = new Message();
		reply.setFrom("scheduling.localhost.localdomain");
		reply.setTo(arg0.getFrom());
		String subject = "";
		String body = "";
		Log.error("Received message!");
		Log.error("Message:" + arg0.toXML());
        //parse the xml:
		Hashtable<String, String> pktTable=this.parseXML(arg0.toXML());
		if (arg0.getID().equals("pullConference")) {
			Log.error("Received pull message!");
			body = databaseService.pullConference(pktTable.get("roomID"));
			subject = "ConferenceInfo";
		}
		else if (arg0.getID().equals("pushConference")) {
			Log.error("Received push message!");
			body=databaseService.push(pktTable);
			
			subject = "ConferencePushResult";
		} else {
			Log.error("Woe betide us all!");
			body = "Invalid packet.";
			subject = "Error";
		}
		reply.setSubject(subject);
		reply.setBody(body);
		try {
			componentManager.sendPacket(this, reply);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
	}

	// PropertyEventListener Interface
	@Override
	public void propertyDeleted(String arg0, Map<String, Object> arg1) {
		if (arg0.equals("plugin.scheduling.serviceName")) {
			changeServiceName("scheduling");
		}
	}

	@Override
	public void propertySet(String arg0, Map<String, Object> arg1) {
		if (arg0.equals("plugin.scheduling.serviceName")) {
			changeServiceName((String)arg1.get("value"));
		}
	}

	@Override
	public void xmlPropertyDeleted(String arg0, Map<String, Object> arg1) {}

	@Override
	public void xmlPropertySet(String arg0, Map<String, Object> arg1) {}

	 /**
     * Changes the service name to a new value. Taken from broadcast plugin.
     *
     * @param serviceName the service name.
     */
    private void changeServiceName(String serviceName) {
         if (serviceName == null) {
            throw new NullPointerException("Service name cannot be null");
        }
        if (this.serviceName.equals(serviceName)) {
            return;
        }

        // Re-register the service.
        try {
            componentManager.removeComponent(this.serviceName);
        }
        catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
        try {
            componentManager.addComponent(serviceName, this);
        }
        catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
        this.serviceName = serviceName;
    }
}
