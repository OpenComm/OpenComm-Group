package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.graphics.drawable.BitmapDrawable;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.PopupNotificationController;


public class PopupNotificationView extends LinearLayout {

	private static String LOG_TAG = "OC_PopupNotificationView"; // for error checking
    private Context context;
    private LayoutInflater inflater;
    private PopupNotificationController popupNotificationController;
    
    private LinearLayout layout; //The layout representing the popup
    private PopupWindow popup; //The window containing the popup
    private ImageButton goButton; //A reference to the Go Button

    private String headerText;
    private String line1;
    private String line2;
    private int type;
    private String[] args;

    
    //0 is tip, 1 is notification, 2 is confirmation for type
    /**
     * Constructor
     * @param context - MainApplication
     * @param args - arguments to pass to the button handler for confirmation popup controllers. null if not needed 
     * @param head - text to display in tip header
     * @param line1 - text to display in line 1 of popup (holds about 60 chars, or 40 chars for confirmation type)
     * @param line2 - text to display in line 2 of popup (holds about 60 chars, or 40 chars for confirmation type)
     * @param type - type of popup to display, 0 for tip, 1 for notification, and 2 for confirmation (with button)
     */
    public PopupNotificationView(Context context, String[] args, String head, String line1, String line2, int type) {
    	super(context);
    	popupNotificationController = new PopupNotificationController(this);
    	this.args = args;
    	this.headerText = head;
    	this.line1 = line1;
    	this.line2 = line2;
    	this.type = type;
    	this.context = context;
    	this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	if(type != Values.confirmation) {
    		this.layout = (LinearLayout) inflater.inflate(R.layout.popup_notification_layout, this);
    	}
    	else {
    		this.layout = (LinearLayout) inflater.inflate(R.layout.popup_confirmation_layout, this);
    	}
    	
    	initEventsAndProperties();
    }

    /**
     *initializes properties of popup
     */
    public void initEventsAndProperties() {
    	if (inflater != null) {
    		TextView t = new TextView(context);
    		t = (TextView)findViewById(R.id.header);
    		t.setText(headerText);
    		if(type == Values.tip) {
    			t.setTextColor(getResources().getColor(R.color.blue));
    			Log.d(LOG_TAG, "TYPE: TIP");	
    		}
    		else if(type == Values.notification) {
    			t.setTextColor(getResources().getColor(R.color.green));
    			Log.d(LOG_TAG, "TYPE: NOTIFICATION");
    		}
    		else if(type == Values.confirmation) {
    			t.setTextColor(getResources().getColor(R.color.green));
    			Log.d(LOG_TAG, "TYPE: CONFIRMATION");
    		}
    		t = (TextView)findViewById(R.id.line1);
    		t.setText(line1);
    		t = (TextView)findViewById(R.id.line2);
    		t.setText(line2);
    	}
    		initializeEvents();
    }
    
    /**
     *Called to display popup on screen
     */
    public void createPopupWindow() {
    	int height;
    	if (line2.equals(""))
    		height = 90;
    	else
    		height = 112;
        popup = new PopupWindow(this, Values.screenW, height, false);
        popup.setTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        if (type != Values.confirmation) {
        	popup.setOutsideTouchable(true);
        }
		popup.showAtLocation(layout, Gravity.BOTTOM, 0, 150);
    }
    /**
     * gets the 
     * @return imgb - the confirm button associated with this popup, if type is confirmation, null otherwise
     */
    public ImageButton getButton() {
    	ImageButton imgb = null;
    	if(type == Values.confirmation && layout != null) {
    		imgb = (ImageButton) layout.findViewById(R.id.gotoButton);
    	}
    	return imgb;
    }
    /**
     * initializes confirmation button click handler for confirmation type popups
     */
	private void initializeEvents() {
		goButton = getButton();
		if(goButton != null) {
			goButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Log.d(LOG_TAG, "Confirm click");
					findViewById(R.id.popOverlay).setVisibility(View.VISIBLE);
					popupNotificationController.handleOkButtonClicked(args);
					popup.dismiss();
				}
			});
		}
	}
	/**
	 * MAKE SURE TO SET THIS
	 * @param pnc - the custom controller to use for this popup
	 */
	public void setController(PopupNotificationController pnc) {
		this.popupNotificationController = pnc;
	}
	/**
	 * returns popupwindows associated with this view
	 */
	public PopupWindow getPopup() {
		return popup;
	}
}


