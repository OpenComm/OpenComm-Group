package edu.cornell.opencomm.controller;

import android.util.Log;
import edu.cornell.opencomm.view.PopupNotificationView;

public class PopupNotificationController {
	
	protected static String LOG_TAG = "OC_PopupNotificationController";
	protected PopupNotificationView  popupNotificationView = null;

	public PopupNotificationController(PopupNotificationView popupNotificationView) {
		this.popupNotificationView = popupNotificationView;
	    }
	
	public void handleOkButtonClicked() {
		Log.d(LOG_TAG, "Button clicked");
	}
}
