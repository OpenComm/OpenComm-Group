package edu.cornell.opencomm.controller;

import android.util.Log;
import edu.cornell.opencomm.view.PopupNotificationView;

public class GoToConferencePopupController extends PopupNotificationController {

	public GoToConferencePopupController(
			PopupNotificationView popupNotificationView) {
		super(popupNotificationView);
	}
	
	public void handleOkButtonClicked() {
		Log.d(LOG_TAG, "Go To Conference");
	}

}
