package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListing {
    Conference conference;
    View conferenceDetails;
    View conferenceHeader;
    boolean current;

    public ConferenceListing(Context context, Conference conference) {
        this.conference = conference;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conferenceDetails = inflater.inflate(R.layout.conference_details_layout, null);
        if(conference.isNow()) {
            conferenceHeader = inflater.inflate(R.layout.current_conference_listing_layout, null);
        } else {
            conferenceHeader = inflater.inflate(R.layout.future_conference_listing_layout, null);
        }
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
        while(iter.hasNext() && count < 3) {
            builder.append("," + iter.next());
        }
        if(iter.hasNext()) {
            count = contacts.size() - 3;
            builder.append("and " + count + " more");
        }
        return builder.toString();
    }

    private void setContacts(Collection<String> contacts) {
        TextView contactsField = (TextView)conferenceHeader.findViewById(R.id.contactList);
        contactsField.setText(getContactString(contacts));
    }

    private void setDate(Date newDate) {
        TextView date = (TextView)conferenceHeader.findViewById(R.id.date);
        //layout for current conferences has no date
        if(date != null) {
            StringBuilder builder = new StringBuilder(newDate.getMonth());
            builder.append("/");
            builder.append(newDate.getDate());
            builder.append(" ");
            boolean AM = true;
            int hours = newDate.getHours();
            if(newDate.getHours() > 12) {
                AM = false;
                hours -= 12;
            }
            builder.append(hours);
            builder.append(":");
            builder.append(newDate.getMinutes());
            if(AM)
                builder.append("AM");
            else
                builder.append("PM");
            date.setText(builder.toString());
        }
    }

    private void setNotes(String newNotes) {
        TextView notes = (TextView)conferenceHeader.findViewById(R.id.notes);
        notes.setText(newNotes);
    }

    public void update() {
        //TODO: Talk to backend about this stuff
        setConferenceTitle(conference.getRoom());
        ArrayList<String> contacts = new ArrayList<String>();
        contacts.add(conference.getInviter());
        setContacts(contacts);
        setDate(conference.getStartDate());
        //setNotes(conference.getNotes());
    }
}
