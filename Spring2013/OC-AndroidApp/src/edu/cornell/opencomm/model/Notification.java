package edu.cornell.opencomm.model;

import java.util.ArrayList;

/** Model for notification. Features include:
 * <ul>
 * <li>Whether the notification has been read yet</li>
 * <li>Notification text</li>
 * <li>Conference model</li>
 * </ul>
 * @author Risa Naka [frontend]
 *
 */
public class Notification {
	
	/** STATIC: a linked list of all notifications, in order that it is received */
	private static ArrayList<Notification> allNotifications = new ArrayList<Notification>();
	
	private String message;
	private boolean read;
	private Conference conf;

	public Notification(String message, Conference conf) {
		this.message = message;
		this.read = false;
		this.conf = conf;
		Notification.allNotifications.add(this);
	}
	
	public static ArrayList<Notification> getAllNotifications() {
		return Notification.allNotifications;
	}
	
	/** Set this notification as read */
	public void readNotification() {
		this.read = true;
	}
	
	/** Returns if this notification has been read yet */
	public boolean isRead() {
		return this.read;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public Conference getConference() {
		return this.conf;
	}
}
