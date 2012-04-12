package edu.cornell.opencomm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;

import android.widget.DatePicker;

import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferencePlannerController;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListing {
    LayoutInflater inflater;
    Context context;
    Conference conference;
    View conferenceDetails;
    View conferenceHeader;
    boolean current;
    TextView datePicker;

    public ConferenceListing(Context context, Conference conference) {
        this.conference = conference;
        this.context = context;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conferenceDetails = inflater.inflate(R.layout.conference_details_layout, null);
        if(conference.isNow()) {
            conferenceHeader = inflater.inflate(R.layout.current_conference_listing_layout, null);
        } else {
            conferenceHeader = inflater.inflate(R.layout.future_conference_listing_layout, null);
        }
        initListeners();
        
        update();
    }

    
    public Conference getConference() {
        return conference;
    }

    public View getConferenceDetailsView() {
        return conferenceDetails;
    }

    public View getConferenceHeaderView() {
        return conferenceHeader;
    }

    private void setConferenceTitle(String title) {
        TextView conferenceTitle = (TextView)conferenceHeader.findViewById(R.id.conferenceTitle);
        conferenceTitle.setText(title);
        conferenceTitle.invalidate();
    }

    public void setPointer(boolean open) {
        ImageView pointer = (ImageView)conferenceHeader.findViewById(R.id.pointer);
        if(open)
            pointer.setImageResource(R.drawable.openconfinfo);
        else
            pointer.setImageResource(R.drawable.closeconfinfo);
    }

    private String getContactString(Collection<String> contacts) {
        Iterator<String> iter = contacts.iterator();
        if(!iter.hasNext())
            return "No contacts";
        StringBuilder builder = new StringBuilder(iter.next());
        int count = 1;
        while(iter.hasNext() && count++ < 3) {
            builder.append(", " + iter.next());
        }
        if(iter.hasNext()) {
            count = contacts.size() - 3;
            builder.append(" and " + count + " more");
        }
        return builder.toString();
    }

    private void setContacts(ArrayList<String> contacts) {
        TextView contactsField = (TextView)conferenceHeader.findViewById(R.id.contactList);
        contactsField.setText(getContactString(contacts));

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup attendeesList = (ViewGroup)conferenceDetails.findViewById(R.id.attendeesList);
        for(int i = 0; i < Math.min(contacts.size(), 3); i++) {
            View contact = inflater.inflate(R.layout.attendee_layout, null);
            TextView nameView = (TextView)contact.findViewById(R.id.name);
            nameView.setText(contacts.get(i));
            //TODO: Replace nora's smiling face with a more appropriate one
            attendeesList.addView(contact);
        }
    }

    private void setStartDate(Date newDate) {
        Date now = Calendar.getInstance().getTime();

        String dateString;
        if(now.getYear() == newDate.getYear())
            dateString = "M/dd h:mma";
        else
            dateString = "yyyy/M/dd h:mma";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateString);
        TextView headerDate = (TextView)conferenceHeader.findViewById(R.id.date);
        String date = dateFormat.format(newDate).toString();

        if(headerDate != null)
            headerDate.setText(date);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma");
        TextView startTime = (TextView)conferenceDetails.findViewById(R.id.startTime);
        startTime.setText(timeFormat.format(newDate).toString());
    }

    private void setEndDate(Date newDate) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma");
        TextView detailsDate = (TextView)conferenceDetails.findViewById(R.id.endTime);
        detailsDate.setText(timeFormat.format(newDate).toString());
    }

    private void setNotes(String newNotes) {
        TextView notes = (TextView)conferenceHeader.findViewById(R.id.notes);
        notes.setText(newNotes);
    }

    public void update() {
        //TODO: Talk to backend about this stuff
        setConferenceTitle(conference.getRoom());
        setContacts(conference.getContactList());
        setStartDate(conference.getStartDate());
        setEndDate(conference.getEndDate());

        //setNotes(conference.getNotes());
    }

    public void initListeners() {
    	ImageView edit=(ImageView) conferenceDetails.findViewById(R.id.pencil);
        Log.v("Crystal", "set listener");
        final Conference conf= this.conference;
    	edit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.v("Crystal", "editing conf info");
			         if(conf== null){
			        	 Log.v("Crystal", "conference is null");
			         }
			         
			        // Crystal:have to hard-code for now for debugging
			         ConferencePlannerView cpv1=new ConferencePlannerView(inflater,context,conf);
			         conf.setPlannerView(cpv1);
			         String date=String.valueOf(conf.getStartMonth())+"/"+String.valueOf(conf.getStartDay())+"/"+String.valueOf(conf.getStartYear());
    	             String startT=String.valueOf(conf.getStartHour())+":"+String.valueOf(conf.getStartMinute());
    	             String endT=String.valueOf(conf.getEndHour())+":"+String.valueOf(conf.getEndMinute());
			         conf.getPlannerView().debugLaunch(conf.getRoom(), date, startT,endT);
			          //cpv1.getConferencePlannerController().buddySelection;
			         CharSequence[] buddyList=conf.getInvitees();//cpv1.getConferencePlannerController().buddyList;
			         final  LinearLayout vs=(LinearLayout) cpv1.getView().findViewById(R.id.userIcons);
			         for (int i = 0; i < buddyList.length; i++) {
							
								Log.v("c","drawing");
						     ConferencePlannerController.addUserIcon((String)buddyList[i], context,vs);
						    
					    }
			         
			         conf.getPlannerView().launch();
			   
				
			}
    		
    	});
    }
        
}

