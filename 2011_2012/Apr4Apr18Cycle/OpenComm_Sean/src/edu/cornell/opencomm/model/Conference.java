package edu.cornell.opencomm.model;

import java.util.Calendar;
import java.util.Date;

import edu.cornell.opencomm.view.ConferencePlannerView;

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
    // the associated conferencePlannerView
    private ConferencePlannerView cpv;

    //Sets conference begin times, and invite list
    public Conference(int startYear, int startMonth,int startDay, int startHour, int startMinute, int endHour, int endMinute, String[] inviteInfo, String inviter){
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

    //Just as above, but with room.
    //TODO: Consider refactoring into 1 constructor.
    public Conference(int startYear, int startMonth,int startDay, int startHour, int startMinute, int endHour, int endMinute, String[] inviteInfo, String inviter, String room) {
        this(startYear, startMonth, startDay, startHour, startMinute, endHour, endMinute, inviteInfo, inviter);
        this.room = room;
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

    public void setPlannerView(ConferencePlannerView cpv) {
        this.cpv = cpv;
    }

    public ConferencePlannerView getPlannerView() {
        return cpv;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public String[] getInviteInfo() {
        return inviteInfo;
    }

    public void setInviteInfo(String[] inviteInfo) {
        this.inviteInfo = inviteInfo;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public Date getStartDate() {
        Calendar startCal = Calendar.getInstance();
        startCal.set(getStartYear(), getStartMonth(), getStartDay(), getStartHour(), getStartMinute());
        return startCal.getTime();
    }

    public Date getEndDate() {
        Calendar endCal = Calendar.getInstance();
        endCal.set(getStartYear(), getStartMonth(), getStartDay(), getEndHour(), getEndMinute());
        return endCal.getTime();
    }

    public boolean isNow() {
        //TODO: Should check if the conference is actually still happening
        //via a network call to server, rather than using approximate end time.
        Date now = Calendar.getInstance().getTime();
        Date startTime = getStartDate();
        Date endTime = getEndDate();

        return (startTime.before(now) || startTime.equals(now) &&
                endTime.before(now) || endTime.equals(now));
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(String room) {
        this.room = room;
    }
}
