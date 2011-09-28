package net.opencomm.smackTester;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

public class OCListener implements PacketListener {
	OCManager manager;
	public OCListener(OCManager manager) {
		this.manager=manager;
	}
	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getClass()) {}
		//arg0.getFrom(), arg0.toString();
		
	}

}
