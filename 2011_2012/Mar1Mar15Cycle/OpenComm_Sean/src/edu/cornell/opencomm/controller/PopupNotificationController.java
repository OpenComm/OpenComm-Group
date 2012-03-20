package edu.cornell.opencomm.controller;

import android.util.Log;
import edu.cornell.opencomm.view.PopupNotificationView;

public class PopupNotificationController {
	
	private static String LOG_TAG = "OC_PopupNotificationController";
	private PopupNotificationView  popupNotificationView = null;

	public PopupNotificationController(PopupNotificationView popupNotificationView) {
		this.popupNotificationView = popupNotificationView;
	    }
	
	public void handleOkButtonClicked() {
		Log.d(LOG_TAG, "Button clicked");
	}
}
