package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.packet.VCard;

import android.graphics.Point;
import android.util.Log;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

public class ConferenceRoom extends MultiUserChat {

	String TAG = "ConferenceRoom";

	private ArrayList<ConferenceUser> confUserList = new ArrayList<ConferenceUser>();

	private static String rName;
	private String roomId;
	private User moderator;

	public ConferenceRoom(String roomId) {
		super(NetworkService.getInstance().getConnection(), roomId);
		this.roomId = roomId;
	}

	private static String formatRoomName(String roomName) {
		roomName = roomName + "@conference.cuopencomm";
		rName = roomName;
		return roomName;
	}

	public ConferenceRoom(Connection c, String s, User u) {
		super(c, formatRoomName(s));
		roomId = s;
		moderator = u;
		retriveOccupants();
	}

	public void join(String nickname) throws XMPPException {
		super.join(nickname);
		Collection<Affiliate> owners = this.getOwners();
		for (Affiliate a : owners) {
			if (a.getJid().equals(UserManager.PRIMARY_USER.getUsername())) {
				this.grantModerator(UserManager.PRIMARY_USER.getNickname());
			}
		}
	}

	public void addUserByJid(String u) {
		VCard user_data = new VCard();
		try {
			user_data.load(NetworkService.getInstance().getConnection(), u);
		} catch (XMPPException e) {
			Log.v(TAG, "couldn't get new user's info from VCard");
			Log.v(TAG, "User is :" + u);
		}
		User added = new User(user_data.getFirstName(),
				user_data.getLastName(), user_data.getEmailHome(), null, null,
				u, user_data.getNickName());
		addUser(added);
	}

	public ConferenceRoom(Connection c, String s) {
		super(c, formatRoomName(s));
		roomId = s;
		retriveOccupants();

		this.addParticipantStatusListener(new ParticipantStatusListener() {

			public void adminGranted(String arg0) {
				// TODO Auto-generated method stub
			}

			public void adminRevoked(String participant) {
			}

			public void banned(String participant, String actor, String reason) {
			}

			public void joined(String participant) {
				// TODO Auto-generated method stub
				addUserByJid(participant);
			}

			public void kicked(String participant, String actor, String reason) {
				// TODO
			}

			public void left(String participant) {
				// TODO Auto-generated method stub
			}

			public void membershipGranted(String participant) {
			}

			public void membershipRevoked(String participant) {
			}

			public void moderatorGranted(String participant) {
			}

			public void moderatorRevoked(String participant) {
			}

			public void nicknameChanged(String participant, String newNickname) {
			}

			public void ownershipGranted(String participant) {
			}

			public void ownershipRevoked(String participant) {
			}

			public void voiceGranted(String participant) {
			}

			public void voiceRevoked(String participant) {
			}
		});
	}

	public void init(boolean isMod) {
		if (isMod) {
			try {
				super.join(UserManager.PRIMARY_USER.nickname);
			} catch (XMPPException e) {
				Log.e(TAG, "could not create room", e);
			}

			// Configure room
			Form form;
			Form answerForm = null;
			try {
				form = this.getConfigurationForm();
				answerForm = form.createAnswerForm();
				for (Iterator<FormField> fields = form.getFields(); fields
						.hasNext();) {
					FormField field = (FormField) fields.next();
					if (!FormField.TYPE_HIDDEN.equals(field.getType())
							&& field.getVariable() != null) {
						answerForm.setDefaultAnswer(field.getVariable());
					}
				}
				answerForm.setAnswer("muc#roomconfig_moderatedroom", true);
			} catch (XMPPException e1) {
				Log.e(TAG, "Could not get configuration form");
			}
			try {
				this.sendConfigurationForm(answerForm);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not send configuration form");
			}
		} else {
			try {
				this.join(rName);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not join room", e);
			}
		}
		// Create and instantiate all existing users
		Iterator<String> occItr = this.getOccupants();
	}

	private void retriveOccupants() {
		// Kris/BE: get the list of occupants from the super/muc and populate
		// the participant maps
	}

	public String getRoomID() {
		return roomId;
	}

	public void setList(ArrayList<User> users) {
		for (User user : users) {
			ConferenceUser cu = new ConferenceUser(user);
			this.confUserList.add(cu);
		}
	}

	public void addUser(User u) {
		ConferenceUser cu = new ConferenceUser(u);
		this.confUserList.add(cu);
	}

	public User getModerator() {
		return moderator;
	}

	// NOTE: cannot revoke privileges, so when leaving, just grant
	// privileges to someone else and then leave.
	public void updateMod(User currMod, User newMod) {
		try {
			super.grantModerator(newMod.getNickname());
			setModerator(newMod);
		} catch (XMPPException e) {
			Log.e(TAG, "Could not transfer privileges", e);
		}
	}

	public void setModerator(User u) {
		moderator = u;
	}

	// public void addConferenceUser(ConferenceUser confUser){
	// confUserList.add(confUser);
	// }
	public ArrayList<ConferenceUser> getCUserList() {
		return confUserList;
	}

	public HashMap<String, User> getUserMap() {
		HashMap<String, User> tmp = new HashMap<String, User>();
		for (ConferenceUser u : confUserList) {
			tmp.put(u.getUser().getUsername(), u.getUser());
		}
		return tmp;
	}

	public HashMap<String, ConferenceUser> getConferenceUserMap() {
		HashMap<String, ConferenceUser> tmp = new HashMap<String, ConferenceUser>();
		for (ConferenceUser u : confUserList) {
			tmp.put(u.getUser().getUsername(), u);
		}
		return tmp;
	}

	public ArrayList<ConferenceUser> updateLocations(Point center, int radius) {
		int noOfusers = confUserList.size();
		ArrayList<Point> pointList = getPoints(noOfusers, radius, center);
		for (int i = 0; i < pointList.size(); i++) {

			// confUserList.get(i).LOCATION = pointList.get(i);
			confUserList.get(i).setLocation(pointList.get(i));
			Log.d("ConfUser-xy - point", "x = " + pointList.get(i).x + ", y = "
					+ pointList.get(i).y);
			Log.d("ConfUser-xy", "x = " + confUserList.get(i).getX() + ", y = "
					+ confUserList.get(i).getY());
		}
		return confUserList;
	}

	private ArrayList<Point> getPoints(int users, double radius, Point center) {
		Log.d("ConferenceRoom", "Radius :" + radius);
		Log.d("ConferenceRoom", "Center :" + center.toString());
		Log.d("ConferenceRoom", "Users :" + users);
		double startAngle = Math.toRadians(90);
		double endAngle = Math.toRadians(360);
		double slice = 2 * Math.PI / users;
		center.x = center.x - 38;
		center.y = center.y - 38;

		ArrayList<Point> pointList = new ArrayList<Point>();
		for (int i = 0; i < users; i++) {
			double angle = (startAngle + slice * i) % endAngle;
			int newX = (int) (center.x + radius * Math.cos(angle));
			int newY = (int) (center.y + radius * Math.sin(angle));
			Point p = new Point(newX, newY);
			Log.d("ConferenceRoom", "Angle :" + angle);
			Log.d("ConferenceRoom", "Point :" + p);
			pointList.add(p);
		}
		return pointList;
	}

	public void invitationReceived(Connection conn, String room,
			String inviter, String reason, String password, Message message) {
		// TODO Auto-generated method stub

	}
}