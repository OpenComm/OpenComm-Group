import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

public class ConferencePacket extends Packet {
	// NOTE: Packet class has methods "setPacketID", "setTo" and "setFrom",
	// which modifies member variables inherited from the Packet class. These
	// fields are part of the packet's XML "translation", so be aware of whether
	// those methods are relevant or not. For example, having unique PacketIDs
	// could help with comparing packets - check IDs, instead of private fields
	// NOTE: The Date class seems to use current values for any fields not
	// initialized. Therefore, be sure that whatever date objects passed to this
	// class have all fields set, even to 0, (for 00 min or 00 sec), otherwise
	// the class uses the current time/date to fill in non-initialized values
	// holds private fields necessary to rebuild Conference object from Packet
	private String name; 
	// Note: Conference object makes it unclear, so to be specific: "name" is
	//treated as the conference name, and "room" is treated as roomname
	private Date startDate, endDate;
	// Date objects representing start and end time of the conference
	private String inviter;
	private String room; 
	// The conference object does not appear to do anything with the "room"
	// parameter (from what I could tell from the conference code - maybe I was
	// looking at older code).  Anyway the Conference constructor forces me to
	// include it, nonetheless.
	private ArrayList<String> contactList;
	private String roomID;
	// non-necessary fields for Conference object building
	private String recurrence;
	private String description;
	private ArrayList<String> participantNames;
	private boolean packetIDSet;

	// the first part of the constructor is identical to that of the Conference
	// Class, ensuring that we will have the data necessary to reconstruct it in
	// the toObject method. The parameters following "contactList"
	// are for the sole purpose of communicating information to the server
	public ConferencePacket(String name, Date startDate, Date endDate,
			String inviter, String room, ArrayList<String> contactList,
			String recurrence, String description, String roomID,
			ArrayList<String> participantNames) {
		packetIDSet = false;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.inviter = inviter;
		this.room = room;
		this.contactList = contactList;
		this.recurrence = recurrence;
		this.description = description;
		this.roomID = roomID;
		this.participantNames = participantNames; 
		// database asks for the names of all users participating in the conference.
		addProperties();

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
	private String formatDate(Date date) {
		// the date.toString() method could be used, but from a quick test, it
		// would not return the proper values that the date object was actually
		// given, plus parsing it could get messy
		StringBuilder build = new StringBuilder();
		build.append(date.getYear() + "-");
		int month = date.getMonth();
		String monthStr = "";
		if (month < 10) { 
			// normally, getMonth() returns in format M, not MM for all months
			// < the 10th month. We make this adjustment for all vulnerable values
			monthStr += "0" + month;
		} else {
			monthStr += month;
		}
		build.append(monthStr + "-");
		int day = date.getDate();
		String dayStr = "";
		if (day < 10) {
			dayStr += "0" + day;
		} else {
			dayStr += day;
		}
		build.append(dayStr + " ");
		int hours = date.getHours();
		String hoursStr = "";
		if (hours < 10) {
			hoursStr += "0" + hours;
		} else {
			hoursStr += hours;
		}
		build.append(hoursStr + ":");
		int mins = date.getMinutes();
		String minsStr = "";
		if (mins < 10) {
			minsStr += "0" + mins;
		} else {
			minsStr += mins;
		}
		build.append(minsStr + ":");
		int sec = date.getSeconds();
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
		this.setProperty("roomID", roomID);
		this.setProperty("roomname", name);
		this.setProperty("invitername", inviter);
		this.setProperty("starttime", formatDate(startDate));
		this.setProperty("endtime", formatDate(endDate));
		this.setProperty("recurrence", recurrence);
		this.setProperty("description", description);
		for (int i = 0; i < participantNames.size(); i++) {
			// Adding +1 to i in field1 so first participant is "Participant1"
			String field1 = "Participant" + (i + 1);
			String field2 = participantNames.get(i);
			this.setProperty(field1, field2);
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
		// Note: Remove condition checks as necessary for true "equivalence".
		// For example, if you deem that the description of the conference
		// is irrelevant to equivalence, the description check should be removed
		// to implement proper functionality.
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		// if the other object is a ConferencePacket, cast it to one.
		ConferencePacket other = (ConferencePacket) o;
		// consider: using toXML results - comparing the XML may be more
		// efficient?
		if (!name.equals(other.getName())
				|| !startDate.equals(other.getStartDate())
				|| !endDate.equals(other.getEndDate())
				|| !inviter.equals(other.getInviter())
				|| !room.equals(other.getRoom())
				|| !contactList.equals(other.getContactList())
				|| !recurrence.equals(other.getRecurrence())
				|| !description.equals(other.getDescription())
				|| !roomID.equals(other.getRoomID())
				|| !participantNames.equals(other.getParticipantNames())) {
			return false; 
			// if any fields are not the same between the two objects
		}
		return true;
	}

	/**
	 * @return String - returns a String XML representation of the packet Builds
	 *         an XLM-style string representation of the packet
	 */
	@Override
	public String toXML() {
		// Note: the structure is based off of various models that I've seen
		// online - not being quite familiar with XML general stylistic
		// features, let me know if there are any changes you would like here.
		// Generally, the XML seems pretty flexible - and depending on how we
		// use it, it may need to be more or less specific/structured
		StringBuilder buf = new StringBuilder();
		buf.append("<ConferencePacket ");
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
			buf.append("\n\t<" + propName + "= \"" + getProperty(propName)
					+ "\"/>");
		}
		// Note: Unsure whether XML format should be in <PropertyName =
		// "property"/> or <PropertyName>property</PropertyName> - I have seen
		// both versions online. The following commented-out code does the
		// second variation, the above code does the first variation
		/*
		for (String propName : propertyNames){
			buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">"); 
		}
		*/	
		// Note: the For-each iteration does not garuntee an in-order iteration
		// through the collection. If it is necessary that the elements are
		// printed in the order they are added, we can implement that through a
		// semi-hardcoded property name search. The code for that is commented
		// out below (in two versions - one for each potential format):
		/*
		String propName;
		propName = "roomID";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		propName = "roomname";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		propName = "invitername";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		propName = "starttime";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		propName = "endtime";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		propName = "recurrence";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		propName = "description";
		buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		for(int i = 0; i < participantNames.size(); i++){
			propName = "Participant" + (i+1);
			buf.append("\n\t<" + propName + "= \"" + getProperty(propName) + "\"/>");
		}
		*/
		/*
		String propName;
		propName = "roomID";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		propName = "roomname";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		propName = "invitername";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		propName = "starttime";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		propName = "endtime";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		propName = "recurrence";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		propName = "description";
		buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		for(int i = 0; i < participantNames.size(); i++){
			propName = "Participant" + (i+1);
			buf.append("\n\t<" + propName + ">" + getProperty(propName) + "</" + propName + ">");
		}
		*/
		buf.append("\n</ConferencePacket>");
		return buf.toString();
		
	}

	/**
	 * @return Returns a Conference object that is built from the information
	 *         provided to the Conference packet
	 */
	public Conference toObject() {
		Conference conference = new Conference(name, startDate, endDate,
				inviter, room, contactList); 
		// builds conference from the data stored in the packet's private member fields
		conference.setRoomID(roomID); 
		// this must be manually set since the constructor does not take this value
		return conference;
	}

	// Getter methods are required to be able to check member variables when comparing packet equality.
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return the inviter
	 */
	public String getInviter() {
		return inviter;
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @return the contactList
	 */
	public ArrayList<String> getContactList() {
		return contactList;
	}

	/**
	 * @return the roomID
	 */
	public String getRoomID() {
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
