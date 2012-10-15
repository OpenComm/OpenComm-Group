package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Calendar;

public class Conference {
    private String conferenceTitle, reoccurrence, notes;
    private Calendar startDateAndTime, endDateAndTime;
    private ArrayList<User> attendees;
    
    public Conference(String conferenceTitle, Calendar startDateAndTime, Calendar endDateAndTime, String reoccurrence, String notes, ArrayList<User> attendees){
    	this.conferenceTitle = conferenceTitle;
    	this.startDateAndTime = startDateAndTime;
    	this.endDateAndTime = endDateAndTime;
    	this.reoccurrence = reoccurrence;
    	this.notes = notes;
    	this.attendees = attendees;
    }
    
    public String getConferenceTitle(){
    	return conferenceTitle;
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
    
    public String getNotes(){
    	return notes;
    }
    
    public ArrayList<User> getAttendees(){
    	return attendees;
    }
    
    /** Return all attendees in this form: 'contact 1, contact 2, and 3 more'
     * given the length of the area */
    public String getAttendeesSummary(){
    /*	String attendees_summary = "";
    	for (User user : attendees){
    		attendees_summary << ""
    	}  */
    	return "blah";
    }
    
    /** Return date in example form of 5/12 */
    public String getNumberMonthAndDay(){
    	return startDateAndTime.get(Calendar.MONTH) + "/" + startDateAndTime.get(Calendar.DATE);
    }
    
    /** Return time in example form of 12:00pm */
    public String getTimeHourAndMinute(){
    	return startDateAndTime.get(Calendar.HOUR) + ":" + startDateAndTime.get(Calendar.MINUTE) + (startDateAndTime.get(Calendar.AM_PM)==1 ? "pm" : "am");
    }
}
