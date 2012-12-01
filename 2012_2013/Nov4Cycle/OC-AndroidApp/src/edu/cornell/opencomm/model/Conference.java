package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import edu.cornell.opencomm.packet.ConferencePacket;
import android.os.Parcel;
import android.os.Parcelable;

public class Conference implements Serializable {
    private String conferenceTitle, reoccurrence, description;
    private Calendar startDateAndTime, endDateAndTime;
    private User inviter;
    private ArrayList<User> attendees;
    private boolean hasAcceptedInvite = false;
    
    ConferenceRoom[] conferenceRooms;
    
    // TEMPORARY VARIABLES - TODO need to discuss with backend
    private String inviter_name;
    private String[] attendee_names;
    private int[] attendee_pictures;
    
    // CONSTANTS
    public final static int UPCOMING = 0;
    public final static int HAPPENING_NOW = 1;
    public final static int INVITED = 2;
    public final static int PREVIOUS = 3;
    
    // CONSTRUCTORS
    
    public Conference( String conferenceTitle, 
    				   String description,
    				   Calendar startDateAndTime, 
    				   Calendar endDateAndTime, 
    				   String reoccurrence, 
    				   User inviter,
    				   ArrayList<User> attendees
    				 ) { 
    	this.conferenceTitle = conferenceTitle;
    	this.description = description;
    	this.startDateAndTime = startDateAndTime;
    	this.endDateAndTime = endDateAndTime;
    	this.reoccurrence = reoccurrence;
    	this.inviter = inviter;
    	this.attendees = attendees;
    }
    
    /** Construct a Conference object from a Parcel */
    public Conference(Parcel parcel){
    	hasAcceptedInvite= parcel.readByte() == 1;
    	conferenceTitle = parcel.readString();
		description = parcel.readString();
		startDateAndTime = Calendar.getInstance();
		startDateAndTime.set(Calendar.HOUR_OF_DAY, parcel.readInt());
		startDateAndTime.set(Calendar.MINUTE, parcel.readInt());
		startDateAndTime.set(Calendar.MONTH, parcel.readInt());
		startDateAndTime.set(Calendar.DAY_OF_MONTH, parcel.readInt());
		startDateAndTime.set(Calendar.YEAR, parcel.readInt());
		endDateAndTime = Calendar.getInstance();
		endDateAndTime.set(Calendar.HOUR_OF_DAY, parcel.readInt());
		endDateAndTime.set(Calendar.MINUTE, parcel.readInt());
		endDateAndTime.set(Calendar.MONTH, parcel.readInt());
		endDateAndTime.set(Calendar.DAY_OF_MONTH, parcel.readInt());
		endDateAndTime.set(Calendar.YEAR, parcel.readInt());
		reoccurrence = parcel.readString();
		inviter_name = parcel.readString();
		attendee_names = parcel.createStringArray();
		attendee_pictures = parcel.createIntArray();
    }
    
    // TEMPORARY GETTERS
    public String getInviterName(){
    	return inviter_name;
    }
    
    public String[] getAttendeeNames(){
    	return attendee_names;
    }
    
    public int[] getAttendeePictures(){
    	return attendee_pictures;
    }
    
    // GETTERS
    
    public ConferenceRoom[] getConferenceRooms(){
    	return conferenceRooms;
    }
    
    public String getConferenceTitle(){
    	return conferenceTitle;
    }
    
    public String getDescription(){
    	return description;
    }
    
    public Calendar getStartDateAndTime(){
    	return startDateAndTime;
    }
    
    public Calendar getEndDateAndTime(){
    	return endDateAndTime;
    }
    
    public String getReoccurence(){
    	return reoccurrence;
    }
    
    public User getInviter(){
    	return inviter;
    }
    
    public ArrayList<User> getAttendees(){
    	return attendees;
    }
    
    // SPECIAL GETTERS
    
    /** Return the type of conference to dictate the layout: invited, happening now, or upcoming */
    public int getConferenceType(Calendar currentTime){
    	int conferenceTimeType = getConferenceTimeType(currentTime);
    	if (!hasAcceptedInvite && conferenceTimeType!=PREVIOUS)
    		return INVITED;
    	else
    		return conferenceTimeType;
    }
    
    /** Returns the type of conference according to time: previous, happening now, or upcoming */
    public int getConferenceTimeType(Calendar currentTime){
    	if (currentTime.after(endDateAndTime))
    		return PREVIOUS;
    	else if ((currentTime.equals(startDateAndTime) || currentTime.after(startDateAndTime)) && (currentTime.equals(endDateAndTime) || currentTime.before(endDateAndTime))){
    		return HAPPENING_NOW;
    	}
    	else 
    		return UPCOMING;
    }
    
    /** Return start time in form of '6:00pm, Dec. 29th, 2012' */
    public String getStartDateAndTimeDescription(){
    	return getTimeDescription(startDateAndTime);
    }
    
    /** Return end time in form of '6:00pm, Dec. 29th, 2012' */
    public String getEndDateAndTimeDescription(){
    	return getTimeDescription(endDateAndTime);
    }
    
    /** Return time given calendar object in form of '6:00pm, Dec. 29th, 2012' */
    public String getTimeDescription(Calendar calendar){
    	int hour_of_day = calendar.get(Calendar.HOUR_OF_DAY);
    	return getClockHour(hour_of_day) + ":" + 
    		   getClockMinute(calendar.get(Calendar.MINUTE)) + 
    		   (calendar.get(Calendar.HOUR_OF_DAY)>11  ? "pm" : "am") + ", " +
    		   getAbbreviatedMonth(calendar.get(Calendar.MONTH)) + ". " + 
    		   calendar.get(Calendar.DAY_OF_MONTH) + ", " + 
    		   calendar.get(Calendar.YEAR);
    }
    
    /** If the hour digit is '0' then return a '12'. If hour>12 then return value <=12 */
    public String getClockHour(int hour){
    	if (hour==0)
    		return "12";
    	else if(hour>12)
    		return (hour - 12) + "";
    	else
    		return hour + "";
    }
    
    /** Adds a '0' digit if there if the minutes<10 */
    public String getClockMinute(int minutes){
    	if (minutes<10)
    		return "0" + minutes;
    	return minutes + "";
    }
    
    public String getAbbreviatedMonth(int month){
    	String abbreviated_month="";
    	switch(month){
    	case(Calendar.JANUARY):
    		abbreviated_month = "Jan";
    		break;
    	case(Calendar.FEBRUARY):
    		abbreviated_month = "Feb";
    		break;
    	case(Calendar.MARCH):
    		abbreviated_month = "Mar";
    		break;
    	case(Calendar.APRIL):
    		abbreviated_month = "Apr";
    		break;
    	case(Calendar.MAY):
    		abbreviated_month = "May";
    		break;
    	case(Calendar.JUNE):
    		abbreviated_month = "June";
    		break;
    	case(Calendar.JULY):
    		abbreviated_month = "July";
    		break;
    	case(Calendar.AUGUST):
    		abbreviated_month = "Aug";
    		break;
    	case(Calendar.SEPTEMBER):
    		abbreviated_month = "Sep";
    		break;
    	case(Calendar.OCTOBER):
    		abbreviated_month = "Oct";
    		break;
    	case(Calendar.NOVEMBER):
    		abbreviated_month = "Nov";
    		break;
    	case(Calendar.DECEMBER):
    		abbreviated_month = "Dec";
    		break;
    	}
    	return abbreviated_month;
    }
    
    // SETTERS
    public void setConferenceRooms(ConferenceRoom[] conferenceRooms){
    	this.conferenceRooms = conferenceRooms;
    }
    
    public void setConferenceRoom(ConferenceRoom room, int position){
    	this.conferenceRooms[position] = room;
    }
    
    public void acceptInvite(){
    	hasAcceptedInvite = true;
    }
	
    // PARCELABLE REQUIRED METHODS
    
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (hasAcceptedInvite ? 1 : 0)); 
		dest.writeString(conferenceTitle);
		dest.writeString(description);
		dest.writeInt(startDateAndTime.get(Calendar.HOUR_OF_DAY));
		dest.writeInt(startDateAndTime.get(Calendar.MINUTE));
		dest.writeInt(startDateAndTime.get(Calendar.MONTH));
		dest.writeInt(startDateAndTime.get(Calendar.DAY_OF_MONTH));
		dest.writeInt(startDateAndTime.get(Calendar.YEAR));
		dest.writeInt(endDateAndTime.get(Calendar.HOUR_OF_DAY));
		dest.writeInt(endDateAndTime.get(Calendar.MINUTE));
		dest.writeInt(endDateAndTime.get(Calendar.MONTH));
		dest.writeInt(endDateAndTime.get(Calendar.DAY_OF_MONTH));
		dest.writeInt(endDateAndTime.get(Calendar.YEAR));
		dest.writeString(reoccurrence);
		dest.writeString(inviter.getNickname());
		String[] attendee_names = new String[attendees.size()];
		int[] attendee_pictures = new int[attendees.size()];
		for (int i=0 ; i < attendees.size(); i++){
			attendee_names[i] = attendees.get(i).getNickname();
//			TODO fix to paint from bitmap
			attendee_pictures[i] = attendees.get(i).getImage();
		}
		dest.writeStringArray(attendee_names);
		dest.writeIntArray(attendee_pictures);
	}
	
	public ConferencePacket toPacket() {
		return new ConferencePacket(conferenceTitle, startDateAndTime,
				endDateAndTime, reoccurrence, inviter, description, attendees);
	}
	
	 @SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR =
		    	new Parcelable.Creator() {
		            public Conference createFromParcel(Parcel in) {
		                return new Conference(in);
		            }
		 
		            public Conference[] newArray(int size) {
		                return new Conference[size];
		            }
		        };
}
