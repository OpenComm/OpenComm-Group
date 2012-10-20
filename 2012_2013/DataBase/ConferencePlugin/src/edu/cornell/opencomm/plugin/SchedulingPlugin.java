package edu.cornell.opencomm.plugin;

import java.io.File;
import java.util.Map;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import org.jivesoftware.smack.packet.Packet;

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

	@Override
	public void processPacket(Packet arg0) {
		Message reply = new Message();
		reply.setFrom("scheduling.localhost.localdomain");
		reply.setTo(arg0.getFrom());
		String subject = "";
		String body = "";
		Log.error("Received message!");
		Log.error("Message:" + arg0.toXML());

		if (arg0.getPacketID().equals("pullConference")) {
			Log.error("Received pull message!");
			body = databaseService.pullConference((String)arg0.getProperty("roomID"));
			subject = "ConferenceInfo";
		}
		else if (arg0.getPacketID().equals("pushConference")) {
			Log.error("Received push message!");
			body=databaseService.push(arg0);
			/*body = databaseService.push(((Message) arg0).getBody()) ? "Success!"
					: "Failure...";*/
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
