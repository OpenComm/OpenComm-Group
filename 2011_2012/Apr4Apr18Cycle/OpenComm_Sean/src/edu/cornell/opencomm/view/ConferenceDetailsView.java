package edu.cornell.opencomm.view;

import android.content.Context;
import android.widget.LinearLayout;
import edu.cornell.opencomm.model.Conference;

public class ConferenceDetailsView extends LinearLayout {

    private Conference conference;

    public ConferenceDetailsView(Context context, Conference conf) {
        super(context);
    }

}
