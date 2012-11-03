package edu.cornell.opencomm.packet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;

//NOTE: Not yet fully integrated w/ Crystal and database.  Will have that all ready by tomorrow night

public class ConferencePacket extends Packet {
	private String name;
	// This is the conference title
	private Calendar startDate, endDate;
	// Calendar objects representing start and end time of the conference
	private String inviterName;
	private User inviter;
	private String recurrence;
	private String description;
	private ArrayList<String> participantNames;
	private ArrayList<User> participants;
	private boolean packetIDSet;
	private static int roomID;
	private String usernamePull;
	private String subject; // indicates whether this packet contains
							// information or is a request for some other action
							// from the database
	private static final String SENDING_INFO_INDICATOR = "pushConference";

	// this is a subject that indicates that this packet contains conference
	// info

	public ConferencePacket(String name, Calendar startDate, Calendar endDate,
			String recurrence, User inviter, String description,
			ArrayList<User> participants) {
		packetIDSet = false;
		this.name = name;
		roomID++; // static room ID allows for unique ids for each conference
					// being sent to database. NOTE: Must decrement this when
					// conferences are removed from database
		this.startDate = startDate;
		this.endDate = endDate;
		this.inviter = inviter;
		this.inviterName = inviter.getUsername();
		this.recurrence = recurrence;
		this.description = description;
		usernamePull = ""; // set to -1 so we can check if other constructor
							// called it
		ArrayList<String> participantNames = new ArrayList<String>();
		for (User user : participants) {
			participantNames.add(user.getUsername());
		}
		this.participants = participants;
		this.participantNames = participantNames;
		// database asks for the names of all users participating in the
		// conference.
		this.subject = SENDING_INFO_INDICATOR; // indicates that this is a
												// conference, not a request for
												// one
		addProperties();
	}

	public ConferencePacket(String username,String subject) {
		this.subject = subject;
		usernamePull =username;
	}

	/**
	 * @param String
	 *            id - Takes in the id that the packetID will be set to
	 * 
	 *            Sets the packetID based on how the superclass method works,
	 *            but does the override in order to also store a boolean
	 *            indicating that this has occured. Otherwise, the default Id is
	 *            set to some set of random characters, which we do not need to
	 *            include in the XML
	 */
	@Override
	public void setPacketID(String id) {
		super.setPacketID(id);
		packetIDSet = true;
	}

	// This method formats the Date to a String "YYYY-MM-DD HH:MM:SS"
	private String formatDate(Calendar date) {
		// the date.toString() method could be used, but from a quick test, it
		// would not return the proper values that the date object was actually
		// given, plus parsing it could get messy
		StringBuilder build = new StringBuilder();
		build.append(date.get(Calendar.YEAR) + "-");
		int month = date.get(Calendar.MONTH);
		String monthStr = "";
		if (month < 10) {
			// normally, getMonth() returns in format M, not MM for all months
			// < the 10th month. We make this adjustment for all vulnerable
			// values
			monthStr += "0" + month;
		} else {
			monthStr += month;
		}
		build.append(monthStr + "-");
		int day = date.get(Calendar.DAY_OF_MONTH);
		String dayStr = "";
		if (day < 10) {
			dayStr += "0" + day;
		} else {
			dayStr += day;
		}
		build.append(dayStr + " ");
		int hours = date.get(Calendar.HOUR_OF_DAY);
		String hoursStr = "";
		if (hours < 10) {
			hoursStr += "0" + hours;
		} else {
			hoursStr += hours;
		}
		build.append(hoursStr + ":");
		int mins = date.get(Calendar.MINUTE);
		String minsStr = "";
		if (mins < 10) {
			minsStr += "0" + mins;
		} else {
			minsStr += mins;
		}
		build.append(minsStr + ":");
		int sec = date.get(Calendar.SECOND);
		String secStr = "";
		if (sec < 10) {
			secStr += "0" + sec;
		} else {
			secStr += sec;
		}
		build.append(secStr);
		return build.toString();
	}

	// This method adds the relevant fields as properties to the packet
	private void addProperties() {
		this.setProperty("subject", subject);
		if (subject.equals(SENDING_INFO_INDICATOR)) {
			// if this is sending info about a conference, send the remainder of
			// the info. If not, send only the username for the pull (usernamePull)
			String roomIDString = "" + roomID;
			this.setProperty("roomID", roomIDString);
			this.setProperty("roomname", name);
			this.setProperty("invitername", inviter);
			this.setProperty("starttime", formatDate(startDate));
			this.setProperty("endtime", formatDate(endDate));
			this.setProperty("recurrence", recurrence);
			this.setProperty("description", description);
			for (int i = 0; i < participantNames.size(); i++) {
				// Adding +1 to i in field1 so first participant is
				// "Participant1"
				String field1 = "participant" + (i + 1);
				String field2 = participantNames.get(i);
				this.setProperty(field1, field2);
				// System.out.println(field2+"\n");
			}
		} else {
			// change to elseIf once more than these two types of packets can be
			// sent to the database (like packets asking to modify information)
			String usernamestring = "" + usernamePull;
			this.setProperty("invitername", usernamestring);
		}
	}

	/**
	 * @param Object
	 *            o - the object to compare this packet to
	 * @return boolean - returns true if the other object is equivalent to this
	 *         packet
	 * 
	 *         This method checks equivalence of each field that the packet
	 *         instantiates in the constructor against the corresponding field
	 *         from the other object. If any are not equivalent, the method
	 *         returns false. If all are, the method returns true
	 */
	public boolean equals(Object o) {
		if (o instanceof ConferencePacket) {
			// if the other object is a ConferencePacket, cast it to one.
			ConferencePacket other = (ConferencePacket) o;
			return (roomID == other.getRoomID());
		}
		return false;
		// Note: design issue - should a user be able to create a conference
		// that is exactly identical to one they have already created? How do we
		// handle that? On this end, they will be processed and stored as two
		// unique conferences and will not fail the equals test

		/*
		 * if (!name.equals(other.getName()) ||
		 * !startDate.equals(other.getStartDate()) ||
		 * !endDate.equals(other.getEndDate()) ||
		 * !inviter.equals(other.getInviter()) ||
		 * !participants.equals(other.getParticipants()) ||
		 * !recurrence.equals(other.getRecurrence()) ||
		 * !description.equals(other.getDescription())) { return false; } if any
		 * fields are not the same between the two objects
		 */
		// Since IDs are unique to each conference, this check should be
		// sufficient

	}

	/**
	 * @return String - returns a String XML representation of the packet Builds
	 *         an XLM-style string representation of the packet
	 */
	public String toXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<message ");
		if (packetIDSet) { // includes only a set Id - not a default
			buf.append("id=\"" + getPacketID() + "\"");
		}
		if (getTo() != null) {
			buf.append(" to=\"").append(StringUtils.escapeForXML(getTo()))
					.append("\"");
		}
		if (getFrom() != null) {
			buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom()))
					.append("\"");
		}
		buf.append(">");
		Collection<String> propertyNames = this.getPropertyNames();

		// write every property and its value to the xml
		for (String propName : propertyNames) {

			if (propName.length() >= 12
					&& propName.substring(0, 11).equals("participant")) {

				buf.append("\n\t<participant>" + getProperty(propName)
						+ "</participant>");
			} else {
				buf.append("\n\t<" + propName + ">" + getProperty(propName)
						+ "</" + propName + ">");
			}
		}

		buf.append("\n</message>");
		return buf.toString();

	}

	/**
	 * @return Returns a Conference object that is built from the information
	 *         provided to the Conference packet
	 */
	public Conference toObject() {
		Conference conference = new Conference(name, description, startDate,
				endDate, recurrence, inviter, participants);
		return conference;
	}

	/**
	 * Decrements the roomID - to be used when conferences removed off database
	 * - when a conference no longer occupies a unique ID value
	 */
	public static void decrementID() {
		roomID--;
	}

	// Getter methods are required to be able to check member variables when
	// comparing packet equality.
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @return the participants
	 */
	public ArrayList<User> getParticipants() {
		return participants;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @return the inviter
	 */
	public User getInviter() {
		return inviter;
	}

	/**
	 * @return the inviter's userName
	 */
	public String getInviterName() {
		return inviterName;
	}

	/**
	 * @return the roomID
	 */
	public int getRoomID() {
		return roomID;
	}

	/**
	 * @return the recurrence
	 */
	public String getRecurrence() {
		return recurrence;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the participantUserIDs
	 */
	public ArrayList<String> getParticipantNames() {
		return participantNames;
	}

}
