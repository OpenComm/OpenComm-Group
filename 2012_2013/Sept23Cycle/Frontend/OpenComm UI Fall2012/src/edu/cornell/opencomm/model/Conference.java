package edu.cornell.opencomm.model;

import java.util.Calendar;

public class Conference {
    private String conferenceTitle, reoccurrence, notes;
    private Calendar startDateAndTime, endDateAndTime;
    private User[] attendees;
    
    public Conference(String conferenceTitle, Calendar startDateAndTime, Calendar endDateAndTime, String reoccurrence, String notes, User[] attendees){
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
    
    public User[] getAttendees(){
    	return attendees;
    }
}
