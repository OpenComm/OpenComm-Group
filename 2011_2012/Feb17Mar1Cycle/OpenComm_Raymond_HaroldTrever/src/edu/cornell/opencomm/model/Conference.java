package edu.cornell.opencomm.model;



//Modedled after Invitation.java, has fields relating to conference, has setters to change them 
public class Conference { 
	//TODO: need getters, as this is being created as a private object 
	// private Connection connection;
	    private String room;
	    private String inviter;
	    private String reason;
	    private String password;
	  //  private Message message;
	    private String[] inviteInfo;
	   // private MultiUserChat muc;
	    private boolean isModeratorRequest;
	    //Fields representing the start time of the conference
	    private int startYear,startMonth,startDay,startHour,startMinute;
	    //Fields representign the end time of the conference (Just hours)
	    private int endHour, endMinute;
	    
	    //Sets conference begin times, and invite list
	    public Conference(int startYear, int startMonth,int startDay, int startHour, int startMinute, int endHour, int endMinute,String[] inviteInfo, String inviter){
	    	this.startYear = startYear;
	    	this.startMonth = startMonth;
	    	this.startDay = startDay;
	    	this.startHour = startHour;
	    	this.startMinute = startMinute;
	    	this.endHour=endHour;
	    	this.endMinute=endMinute;
	    	this.inviteInfo=inviteInfo;
	    	this.inviter = inviter;
	    }
	    
	    //Getters for all fields
	    public int getStartYear(){
	    	return startYear;
	    }
	    public int getStartMonth(){
	    	return startMonth;
	    }
	    public int getStartDay(){
	    	return startDay;
	    }
	    public int getStartHour(){
	    	return startHour;
	    }
	    public int getStartMinute(){
	    	return startMinute;
	    }
	    public int getEndHour(){
	    	return endHour;
	    }
	    public int getEndMinute(){
	    	return endMinute;
	    }
	    //username of the one who created this conference
	    public String getInviter(){
	    	return inviter;
	    }
	    
	    //String array of invited peoples
	    public String[] getInvitees(){
	    	return inviteInfo;
	    }
	    
}
