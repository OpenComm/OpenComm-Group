package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import android.util.Log;

import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ConferencePlannerView;

//Modedled after Invitation.java, has fields relating to conference, has setters to change them
public class Conference implements Comparable<Conference> {
    //TODO: need getters, as this is being created as a private object
    // private Connection connection;
   
	// hashtable for all the conferencePlannerView
    static Hashtable<Integer,ConferencePlannerView> plannerViews= new Hashtable<Integer, ConferencePlannerView>();; 
	private String roomID;
    private String inviter;
   
    private String name;
    private Date startDate, endDate;//Date objects representing start and end time of conference
    private String[]inviteList;
    private ArrayList<String> contactList;
    private static String LOG_TAG = "ConferenceModel";
    private int id;
    // private MultiUserChat muc;

//    //Fields representing the start time of the conference
////    private int startYear,startMonth,startDay,startHour,startMinute;
//    //Fields representign the end time of the conference (Just hours)
//    private int endHour, endMinute;
    // the associated conferencePlannerView
    private ConferencePlannerView cpv;

    //Sets conference begin times, and invite list
//    public Conference(int startYear, int startMonth,int startDay, int startHour, int startMinute, int endHour, int endMinute, String[] inviteInfo, String inviter){
//        this.startYear = startYear;
//        this.startMonth = startMonth;
//        this.startDay = startDay;
//        this.startHour = startHour;
//        this.startMinute = startMinute;
//        this.endHour=endHour;
//        this.endMinute=endMinute;
//        this.inviteInfo=inviteInfo;
//        this.inviter = inviter;
//    }

  //  public Con
    //Just as above, but with room, contacts
    //TODO: Consider refactoring into 1 constructor.
//    public Conference(int startYear, int startMonth,int startDay, int startHour, int startMinute, int endHour, int endMinute, String[] inviteInfo, String inviter, String room, ArrayList<String> contactList) {
//        this(startYear, startMonth, startDay, startHour, startMinute, endHour, endMinute, inviteInfo, inviter);
//        this.room = room;
//        this.contactList = contactList;
//
//    }

    public static Hashtable getPlannerViews() {
		return plannerViews;
	}

	public Conference(String name, Date startDate, Date endDate, String inviter, String room, ArrayList<String> contactList){
    	this.name = name;
		this.startDate=startDate;
    	this.endDate=endDate;
    	this.contactList=contactList;
    	this.inviter=inviter;
    	Log.v(LOG_TAG, "Conference Constructor called!");
    }
    
//    //Getters for all fields
		
	public String getName() {
		return name;
	}
    public int getStartYear(){
        return startDate.getYear();
    }
    public int getStartMonth(){
        return startDate.getMonth();
    }
    public int getStartDay(){
        return startDate.getDay();
    }
    public int getStartHour(){
        return startDate.getHours();
    }
    public int getStartMinute(){
        return startDate.getMinutes();
    }
    public int getEndHour(){
        return endDate.getHours();
    }
    public int getEndMinute(){
        return endDate.getMinutes();
    }
    /**Returns inviter username (does not include server IP) */
    public String getInviter(){
        return inviter;
    }

    //String array of invited peoples
//    public String[] getInvitees(){
//        return inviteInfo;
//    }

    public void setPlannerView(ConferencePlannerView cpv) {
        this.cpv = cpv;
    }

    public ConferencePlannerView getPlannerView() {
        return cpv;
    }

    public void setName(String name) {
    	this.name = name;
    }
    public void setStartDay(int startDay) {
        startDate.setDate(startDay);
    }

    public void setStartHour(int startHour) {
        startDate.setHours(startHour);
    }

//    public String[] getInviteInfo() {
//        return inviteInfo;
//    }
//
//    public void setInviteInfo(String[] inviteInfo) {
//        this.inviteInfo = inviteInfo;
//    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public void setStartYear(int startYear) {
        startDate.setYear(startYear);
    }

    public void setStartMonth(int startMonth) {
        startDate.setMonth(startMonth);
    }

    public void setStartMinute(int startMinute) {
        startDate.setMinutes(startMinute);
    }

    public void setEndHour(int endHour) {
        endDate.setHours(endHour);
    }

    public void setEndMinute(int endMinute) {
        endDate.setMinutes(endMinute);
    }

//    public Date getStartDate() {
//        Calendar startCal = Calendar.getInstance();
//        startCal.set(getStartYear(), getStartMonth(), getStartDay(), getStartHour(), getStartMinute());
//        return startCal.getTime();
//    }

//    public Date getEndDate() {
//        Calendar endCal = Calendar.getInstance();
//        endCal.set(getStartYear(), getStartMonth(), getStartDay(), getEndHour(), getEndMinute());
//        return endCal.getTime();
//    }

    
    public Date getStartDate(){
    	return startDate;
    }
    
    public Date getEndDate(){
    	return endDate;
    }
    public void setStartDate(int year, int month, int day, int hour, int min ){
    	startDate.setYear(year);
    	startDate.setMonth(month);
    	startDate.setDate(day);
    	startDate.setHours(hour);
    	startDate.setMinutes(min);	
    
    }
  
    public void setEndDate(int year, int month, int day, int hour, int min ){
    	endDate.setYear(year);
    	endDate.setMonth(month);
    	endDate.setDate(day);
    	endDate.setHours(hour);
    	endDate.setMinutes(min);	
    
    }
    
    public boolean isNow() {
        //TODO: Should check if the conference is actually still happening
        //via a network call to server, rather than using approximate end time.
        Date now = Calendar.getInstance().getTime();
        Date startTime = getStartDate();
        Date endTime = getEndDate();

        return ((startTime.before(now) || startTime.equals(now)) &&
                (endTime.after(now) || endTime.equals(now)));
    }

    public boolean isFuture() {
        Date now = Calendar.getInstance().getTime();
        Date startTime = getStartDate();
        return startTime.after(now);
    }

    /**
     * @return the room
     */
    public String getRoomID() {
        return roomID;
    }

    /**
     * @param room the room to set
     */
    public void setRoomID(String room) {
        this.roomID = room;
    }

    public ArrayList<String> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<String> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int compareTo(Conference other) {
        if(this == other) return 0;
        Date startDate = getStartDate();
        Date otherStartDate = other.getStartDate();
        return startDate.compareTo(otherStartDate);
    }
    
    /**Returns ararylist of contactlist as a string array*/
    public String[] getContactsAsArray(){
    	return getContactList().toArray(new String[0]);
    }
    
    /**Anything that floats our boat for now: change later */
    public String getRecurring(){
    	return "once";
    }
    
    /**Returns unix time of start date object, for pushing to server */
    public long getStartLong(){
    	return startDate.getTime();
    }
    
    /**Returns unix time of end date object, for pushing to server */
    public long getEndLong(){
    	return endDate.getTime();
    }
    
    /**Returns date in string of format dd/mm/yyyy*/
    public String getDateString(){
    	int day = getStartDay();
    	int month = getStartMonth();
    	int year = getStartYear();
    	String dayString = ""+ day;
    	
    	if(day<10){
    		dayString = "0"+day;
    	}
    	
    	String monthString = ""+ month;
    	if(month<10){
    		monthString = "0"+ month;
    	}
    	
    	return dayString+"/"+ monthString + "/" + year;
    }
    
    /**Returns inviter name + server */
    public String getHostName(){
    	return getInviter() + "@" + Network.DEFAULT_HOSTNAME;//@opencomm-no-ip.org by default
    }

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
    
}
