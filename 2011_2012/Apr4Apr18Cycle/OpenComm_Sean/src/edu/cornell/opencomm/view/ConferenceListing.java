package edu.cornell.opencomm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConferencePlannerController;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListing {
    private LayoutInflater inflater;
    private Context context;
    private Conference conference;
    private View conferenceDetails;
    private View conferenceHeader;
    private Typeface font;

    public ConferenceListing(Context context, Conference conference) {
        this.conference = conference;
        this.context = context;
        font = Typeface.createFromAsset(context.getAssets(), Values.font);

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
        conferenceTitle.setTypeface(font);
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
        contactsField.setTypeface(font);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup attendeesList = (ViewGroup)conferenceDetails.findViewById(R.id.attendeesList);
        for(int i = 0; i < contacts.size(); i++) {
            View contact = inflater.inflate(R.layout.attendee_layout, null);
            TextView nameView = (TextView)contact.findViewById(R.id.name);
            nameView.setText(contacts.get(i));
            nameView.setTypeface(font);
            //TODO: Replace nora's smiling face with a more appropriate one
            attendeesList.addView(contact);
        }
    }

    private void setDate(Date startDate, Date endDate) {
        Date now = Calendar.getInstance().getTime();

        String dateString;
        if(now.getYear() == startDate.getYear())
            dateString = "M/dd h:mma";
        else
            dateString = "yyyy/M/dd h:mma";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateString);
        TextView headerDate = (TextView)conferenceHeader.findViewById(R.id.date);
        String date = dateFormat.format(startDate).toString();

        if(headerDate != null) {
            headerDate.setText(date);
            headerDate.setTypeface(font);
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma");
        TextView time = (TextView)conferenceDetails.findViewById(R.id.time);
        time.setText(timeFormat.format(startDate).toString() +  "-" + timeFormat.format(endDate).toString());
        time.setTypeface(font);
    }

    private void setNotes(String newNotes) {
        TextView notes = (TextView)conferenceHeader.findViewById(R.id.notes);
        notes.setText(newNotes);
        notes.setTypeface(font);
    }

    public void update() {
        //TODO: Talk to backend about this stuff
        setConferenceTitle(conference.getRoom());
        setContacts(conference.getContactList());
        setDate(conference.getStartDate(), conference.getEndDate());

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
			         cpv1.getConferencePlannerController().setAll(conf);
			         String date=String.valueOf(conf.getStartMonth())+"/"+String.valueOf(conf.getStartDay())+"/"+String.valueOf(conf.getStartYear());
    	             String startT=String.valueOf(conf.getStartHour())+":"+String.valueOf(conf.getStartMinute());
    	             String endT=String.valueOf(conf.getEndHour())+":"+String.valueOf(conf.getEndMinute());
			         conf.getPlannerView().debugLaunch(conf.getRoom(), date, startT,endT);
			          //cpv1.getConferencePlannerController().buddySelection;
			         CharSequence[] buddyList=conf.getContactList().toArray(new CharSequence[0]);//getInvitees();//cpv1.getConferencePlannerController().buddyList;
			         final  LinearLayout vs=(LinearLayout) cpv1.getView().findViewById(R.id.userIcons);
			         for (int i = 0; i < buddyList.length; i++) {

								Log.v("c","drawing");
						     ConferencePlannerController.addUserIcon((String)buddyList[i], context,vs);

					    }
                     conf.getPlannerView().getCreateButton().setText("update");
			         conf.getPlannerView().launch();


			}

    	});
    }

}

