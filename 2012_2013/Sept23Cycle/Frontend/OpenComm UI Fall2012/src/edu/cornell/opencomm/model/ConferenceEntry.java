package edu.cornell.opencomm.model;

public class ConferenceEntry {
	private String conferenceTitle;
	private String[] attendees;
	private String date, time;
	
	public String getConferenceTitle(){
		return conferenceTitle;
	}
	
	public void setConferenceTitle(String newConferenceTitle){
		conferenceTitle = newConferenceTitle;
	}
	
	public String[] getAttendees(){
		return attendees;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getTime(){
		return time;
	}
}
