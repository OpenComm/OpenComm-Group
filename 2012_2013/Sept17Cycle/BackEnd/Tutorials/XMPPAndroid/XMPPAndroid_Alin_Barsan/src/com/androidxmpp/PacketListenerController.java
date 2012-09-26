package com.androidxmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.widget.Toast;

public class PacketListenerController {

	public PacketListener packetListener;
	
	public void createPacketListener(final Context context){
		PacketListener packetListener = new PacketListener() {
			public void processPacket(Packet arg0) {
				String message = arg0.toString();
				String text = "A message has been posted on the chat room:\n\n" + message;    			
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    			toast.show();							
			}
		    };
		this.packetListener = packetListener;
	}
	
	public void addPacketListenerTo(MultiUserChat groupChat){
		groupChat.addMessageListener(packetListener);
	}
	
}

