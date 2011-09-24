package opencomm.rtp.bombaysapphire;

public class AudioConnect {
	private UserRTP sender;
	private UserRTP receiver;
	private int port;

	
	public void setSender(UserRTP sender) {
		this.sender = sender;
	}

	public UserRTP getSender() {
		return this.sender;
	}

	public void setReceiver(UserRTP rec) {
		this.receiver = rec;
	}

	public UserRTP getReceiver() {
		return this.receiver;
	}

	public void setPort(int pt) {
		this.port = pt;
	}

	public int getPort() {
		return this.port;
	}

	public AudioConnect(UserRTP s, UserRTP r, int p) {
		this.setSender(s);
		this.setReceiver(r);
		this.setPort(p);
	}
}
