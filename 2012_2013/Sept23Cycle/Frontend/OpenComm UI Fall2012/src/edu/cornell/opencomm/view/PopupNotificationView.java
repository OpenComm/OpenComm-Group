package edu.cornell.opencomm.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    }

    /**
     *initializes properties of popup
     */
    public void initEventsAndProperties() {
    }
    
    /**
     *Called to display popup on screen
     */
    public void createPopupWindow() {
    	//
    }
    /**
     * gets the 
     * @return imgb - the confirm button associated with this popup, if type is confirmation, null otherwise
     */
    public ImageButton getButton() {
    	return null;
    }
    /**
     * initializes confirmation button click handler for confirmation type popups
     */
	private void initializeEvents() {
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
	
	public void dismiss() {
		popup.dismiss();
	}
	
	public void setOverlay(boolean b) {
	}
}


