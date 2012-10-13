package edu.cornell.opencomm.model;

public class ConferenceDetails {
    private String Tag;
    private String startTime, endTime, reoccurrence, notes;
    private String[] attendees;
    
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
    
    public String[] getAttendees(){
    	return attendees;
    }
}
