package edu.cornell.opencomm.model;

public class Conference {
    private String conferenceTitle, date, startTime, endTime, reoccurrence, notes;
    private User[] attendees;
    
    public Conference(String conferenceTitle, String date, String startTime, String endTime, String reoccurrence, String notes, User[] attendees){
    	this.conferenceTitle = conferenceTitle;
    	this.date = date;
    	this.startTime = startTime;
    	this.endTime = endTime;
    	this.reoccurrence = reoccurrence;
    	this.notes = notes;
    	this.attendees = attendees;
    }
    
    public String getConferenceTitle(){
    	return conferenceTitle;
    }
    
    public String getDate(){
    	return date;
    }
    
    public String getStartTime(){
    	return startTime;
    }
    
    public String getEndTime(){
    	return endTime;
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
